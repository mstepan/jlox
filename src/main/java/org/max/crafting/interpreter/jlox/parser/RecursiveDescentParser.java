package org.max.crafting.interpreter.jlox.parser;

import org.max.crafting.interpreter.jlox.Lox;
import org.max.crafting.interpreter.jlox.ast.Assignment;
import org.max.crafting.interpreter.jlox.ast.BinaryExpr;
import org.max.crafting.interpreter.jlox.ast.Block;
import org.max.crafting.interpreter.jlox.ast.CallExpr;
import org.max.crafting.interpreter.jlox.ast.CommaExpr;
import org.max.crafting.interpreter.jlox.ast.Expression;
import org.max.crafting.interpreter.jlox.ast.ExpressionStmt;
import org.max.crafting.interpreter.jlox.ast.FunctionStmt;
import org.max.crafting.interpreter.jlox.ast.Grouping;
import org.max.crafting.interpreter.jlox.ast.IfStmt;
import org.max.crafting.interpreter.jlox.ast.Literal;
import org.max.crafting.interpreter.jlox.ast.LogicalExpr;
import org.max.crafting.interpreter.jlox.ast.PrintStmt;
import org.max.crafting.interpreter.jlox.ast.ReturnStmt;
import org.max.crafting.interpreter.jlox.ast.Stmt;
import org.max.crafting.interpreter.jlox.ast.UnaryExpr;
import org.max.crafting.interpreter.jlox.ast.VarStmt;
import org.max.crafting.interpreter.jlox.ast.VariableExpr;
import org.max.crafting.interpreter.jlox.ast.WhileStmt;
import org.max.crafting.interpreter.jlox.model.Token;
import org.max.crafting.interpreter.jlox.model.TokenType;

import java.util.ArrayList;
import java.util.List;

/**
 * Use recursive descent parser technique, so grammar should be NON left-recursive (see main-grammar.txt).
 */
public class RecursiveDescentParser {

    private final List<Token> tokens;
    private int cur = 0;

    public RecursiveDescentParser(List<Token> tokens) {
        this.tokens = tokens;
    }

    /**
     * program -> (declaration)* EOF
     */
    public List<Stmt> parse() {
        List<Stmt> statements = new ArrayList<>();

        while (!isEnd()) {
            statements.add(declaration());
        }

        return statements;
    }

    public Expression parseSingleExpression() {
        try {
            return expression();
        }
        catch (ParseError parseError) {
            return null;
        }
    }

    /**
     * declaration -> funcDeclaration | varDeclaration | statement
     */
    private Stmt declaration() {
        try {
            if (matchAny(TokenType.FUN)) {
                return funcDeclaration("function");
            }
            if (matchAny(TokenType.VAR)) {
                return varDeclaration();
            }

            return statement();
        }
        catch (ParseError error) {
            synchronize();
            return null;
        }
    }

    /**
     * funcDeclaration -> "fn" function
     *
     * @param type - can be 'function' or 'method'
     */
    private Stmt funcDeclaration(String type) {
        return function(type);
    }

    /**
     * function -> IDENTIFIER "(" parameter? ")" block
     */
    private Stmt function(String type) {

        Token fnName = consume(TokenType.IDENTIFIER, "Expect " + type + "name.");

        consume(TokenType.LEFT_PAREN, "Expected '(' after " + type + " name.");

        // read all parameters if any
        List<Token> parameters = new ArrayList<>();
        if (!check(TokenType.RIGHT_PAREN)) {
            do {
                if (parameters.size() >= MAX_ARGUMENTS_IN_FUNCTION_CALL_THRESHOLD) {
                    error(peek(), "Can't have more than " + MAX_ARGUMENTS_IN_FUNCTION_CALL_THRESHOLD + " parameters.");
                }

                parameters.add(consume(TokenType.IDENTIFIER, "Expected parameter name"));
            }
            while (matchAny(TokenType.COMMA));
        }

        consume(TokenType.RIGHT_PAREN, "Expected ')' after " + type + " parameters.");

        consume(TokenType.LEFT_BRACE, "Expected '{' before " + type + " body.");
        Block fnBody = block();

        return new FunctionStmt(fnName, parameters, fnBody);
    }

    /**
     * varDeclaration -> "var" IDENTIFIER ("=" expression)? ";"
     */
    private Stmt varDeclaration() {

        Token name = consume(TokenType.IDENTIFIER, "Variable name expected.");

        Expression initExpr = null;

        if (matchAny(TokenType.EQUAL)) {
            initExpr = expression();
        }

        consume(TokenType.SEMICOLON, "Expected ';' after variable declaration.");

        return new VarStmt(name, initExpr);
    }

    /**
     * statement -> exprStmt | printStmt | block | ifStatement | whileStatement | returnStatement
     */
    private Stmt statement() {
        if (matchAny(TokenType.PRINT)) {
            return printStatement();
        }
        if (matchAny(TokenType.LEFT_BRACE)) {
            return block();
        }
        if (matchAny(TokenType.IF)) {
            return ifStatement();
        }
        if (matchAny(TokenType.WHILE)) {
            return whileStatement();
        }
        if (matchAny(TokenType.FOR)) {
            return forStatement();
        }
        if (matchAny(TokenType.RETURN)) {
            return returnStatement();
        }

        return expressionStatement();
    }

    /**
     * printStmt -> "print" expression ";"
     */
    private Stmt printStatement() {
        Expression expr = expression();
        consume(TokenType.SEMICOLON, "Expected ';' after print statement.");
        return new PrintStmt(expr);
    }

    /**
     * block -> "{" declaration* "}"
     */
    private Block block() {
        Block blockStmt = new Block();

        while (!check(TokenType.RIGHT_BRACE) && !isEnd()) {
            Stmt singleStmt = declaration();
            blockStmt.add(singleStmt);
        }

        consume(TokenType.RIGHT_BRACE, "'}' expected at the end of block.");

        return blockStmt;
    }

    /**
     * ifStatement -> "if" "(" expression ")" statement ("else" statement)?
     */
    private Stmt ifStatement() {
        consume(TokenType.LEFT_PAREN, "expected '(' after if.");

        Expression conditionExpr = expression();

        consume(TokenType.RIGHT_PAREN, "expected ')' after if condition.");

        Stmt thenBranch = statement();
        Stmt elseBranch = null;

        if (matchAny(TokenType.ELSE)) {
            elseBranch = statement();
        }

        return new IfStmt(conditionExpr, thenBranch, elseBranch);
    }

    /**
     * whileStatement -> "while" "(" expression ")" statement
     */
    private Stmt whileStatement() {

        consume(TokenType.LEFT_PAREN, "'(' expected after while.");

        Expression condition = expression();

        consume(TokenType.RIGHT_PAREN, "')' expected after while condition.");

        Stmt body = statement();

        return new WhileStmt(condition, body);
    }

    /**
     * forStatement -> "for" "(" (varDeclaration | exprStmt | ";") expression? ";" expression?  ")" statement
     * Parse FOR-statement here, but convert into WHILE statement under the hood.
     * FOR loop kinda playing role of 'syntactic sugar' for WHILE loop.
     */
    private Stmt forStatement() {

        consume(TokenType.LEFT_PAREN, "Expected '(' after for.");

        // parse INIT section (init is optional)
        Stmt init;
        if (matchAny(TokenType.SEMICOLON)) {
            init = null;
        }
        else if (matchAny(TokenType.VAR)) {
            init = varDeclaration();
        }
        else {
            init = expressionStatement();
        }

        // parse CONDITION section if any (condition is optional)
        Expression condition;
        if (matchAny(TokenType.SEMICOLON)) {
            condition = null;
        }
        else {
            condition = expression();
            consume(TokenType.SEMICOLON, "Expected ';' after FOR loop condition.");
        }

        // parse INCREMENT section if any (increment is optional)
        Expression increment;
        if (matchAny(TokenType.RIGHT_PAREN)) {
            increment = null;
        }
        else {
            increment = expression();
            consume(TokenType.RIGHT_PAREN, "Expected ')' after FOR clauses.");
        }

        Stmt body = statement();

        return toWhileStatement(init, condition, increment, body);
    }

    /**
     * Convert parts of FOR loop into WHILE loop.
     */
    private Stmt toWhileStatement(Stmt init, Expression condition, Expression increment, Stmt body) {

        Expression whileCondition = (condition == null) ? new Literal(true) : condition;

        Block whileBody = new Block();
        whileBody.add(body);
        if (increment != null) {
            whileBody.add(new ExpressionStmt(increment));
        }

        WhileStmt whileStmt = new WhileStmt(whileCondition, whileBody);

        Block combinedBlock = new Block();
        if (init != null) {
            combinedBlock.add(init);
        }

        combinedBlock.add(whileStmt);

        return combinedBlock;
    }

    /**
     * returnStatement -> "return" expression? ";"
     */
    private Stmt returnStatement() {
        Token keyword = previous();

        Expression expr = null;

        if (!check(TokenType.SEMICOLON)) {
            expr = expression();
        }

        consume(TokenType.SEMICOLON, "Expected ';' after return value.");

        return new ReturnStmt(keyword, expr);
    }


    /**
     * exprStmt -> expression ";"
     */
    private Stmt expressionStatement() {
        Expression expr = expression();
        consume(TokenType.SEMICOLON, "Expected ';' after expression.");
        return new ExpressionStmt(expr);
    }

    /**
     * expression -> comma
     */
    private Expression expression() {
        return comma();
    }

    /**
     * comma = assignment ("," comma)*
     */
    private Expression comma() {

        Expression left = assignment();

        if (matchAny(TokenType.COMMA)) {
            Expression right = comma();
            return new CommaExpr(left, right);
        }

        return left;
    }

    /**
     * assignment -> IDENTIFIER "=" assignment | logic_or
     */
    private Expression assignment() {

        Expression left = logicalOr();

        if (matchAny(TokenType.EQUAL)) {
            Token equalsToken = previous();
            Expression right = assignment();

            if (left instanceof VariableExpr) {
                Token name = ((VariableExpr) left).name;
                return new Assignment(name, right);
            }

            throw error(equalsToken, "Invalid assignment target.");
        }
        return left;
    }

    /**
     * logic_or -> logic_and ("or" logic_and)*
     */
    private Expression logicalOr() {
        Expression expr = logicalAnd();

        while (matchAny(TokenType.OR)) {
            Token operator = previous();
            Expression right = logicalAnd();
            expr = new LogicalExpr(expr, operator, right);
        }

        return expr;
    }

    /**
     * logic_and -> equality ("and" equality)*
     */
    private Expression logicalAnd() {

        Expression expr = equality();

        while (matchAny(TokenType.AND)) {
            Token operator = previous();
            Expression right = equality();
            expr = new LogicalExpr(expr, operator, right);
        }

        return expr;
    }

    /**
     * equality -> comparison  (("==" | "!=") comparison)*
     */
    private Expression equality() {
        Expression expr = comparison();

        while (matchAny(TokenType.EQUAL_EQUAL, TokenType.BANG_EQUAL)) {
            Token op = previous();
            Expression rightExp = comparison();
            expr = new BinaryExpr(expr, op, rightExp);
        }

        return expr;
    }

    /**
     * comparison -> term ((">" | "<=" | "<" | "<=") term)*
     */
    private Expression comparison() {
        Expression expr = term();

        while (matchAny(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
            Token op = previous();
            Expression rightExp = term();
            expr = new BinaryExpr(expr, op, rightExp);
        }

        return expr;
    }

    /**
     * term -> factor (("-" | "+") factor)*
     */
    private Expression term() {

        Expression expr = factor();

        while (matchAny(TokenType.MINUS, TokenType.PLUS)) {
            Token op = previous();
            Expression rightSide = factor();
            expr = new BinaryExpr(expr, op, rightSide);
        }

        return expr;
    }

    /**
     * factor -> unary (("*" | "/" ) unary)*
     */
    private Expression factor() {
        Expression expr = unary();

        while (matchAny(TokenType.STAR, TokenType.SLASH)) {
            Token op = previous();
            Expression rightSide = unary();
            expr = new BinaryExpr(expr, op, rightSide);
        }

        return expr;
    }

    /**
     * unary -> ("!" | "-") unary | call
     */
    private Expression unary() {
        if (matchAny(TokenType.BANG, TokenType.MINUS)) {
            Token op = previous();
            Expression right = unary();
            return new UnaryExpr(op, right);
        }

        return call();
    }

    /**
     * call -> primary ("(" arguments? ")")*
     */
    private Expression call() {

        Expression calleeExpr = primary();

        while (matchAny(TokenType.LEFT_PAREN)) {
            calleeExpr = finishCall(calleeExpr);
        }

        return calleeExpr;
    }

    private static final int MAX_ARGUMENTS_IN_FUNCTION_CALL_THRESHOLD = 255;

    private Expression finishCall(Expression calleeExpr) {

        List<Expression> arguments = new ArrayList<>();

        // no arguments function call possible here
        if (!check(TokenType.RIGHT_PAREN)) {
            do {
                Expression singleArg = expression();
                arguments.addAll(commaToExpressionsList(singleArg));

                if (arguments.size() >= MAX_ARGUMENTS_IN_FUNCTION_CALL_THRESHOLD) {
                    error(peek(), String.format("Arguments list max value reached: %d arguments.",
                                                MAX_ARGUMENTS_IN_FUNCTION_CALL_THRESHOLD));
                }
            }
            while (matchAny(TokenType.COMMA));
        }

        Token rightParen = consume(TokenType.RIGHT_PAREN, "Expected ')' after arguments list.");

        return new CallExpr(calleeExpr, arguments, rightParen);
    }

    /**
     * According to our grammar rules, arguments list will be always parsed to CommaExpr:
     * <p>
     * 1. comma = assignment ("," comma)*
     * 2. arguments -> expression (", " expression)*
     * <p>
     * So just flatten comma expression to list of single expressions.
     */
    private List<Expression> commaToExpressionsList(Expression expression) {

        Expression curExpr = expression;

        List<Expression> flatExpressions = new ArrayList<>();

        while (curExpr instanceof CommaExpr) {

            CommaExpr commaExp = (CommaExpr) curExpr;

            flatExpressions.add(commaExp.left);

            curExpr = commaExp.right;
        }

        flatExpressions.add(curExpr);

        return flatExpressions;
    }


    /**
     * primary -> STRING | NUMBER | "true" | "false" | nil | "(" expression ")" | IDENTIFIER
     */
    private Expression primary() {
        if (matchAny(TokenType.NUMBER, TokenType.STRING)) {
            return new Literal(previous().getLiteral());
        }
        if (matchAny(TokenType.TRUE)) {
            return new Literal(true);
        }
        if (matchAny(TokenType.FALSE)) {
            return new Literal(false);
        }
        if (matchAny(TokenType.NIL)) {
            return new Literal(null);
        }
        if (matchAny(TokenType.LEFT_PAREN)) {
            Expression expr = expression();
            consume(TokenType.RIGHT_PAREN, "Matching ')' expected.");
            return new Grouping(expr);
        }
        if (matchAny(TokenType.IDENTIFIER)) {
            return new VariableExpr(previous());
        }

        throw error(previous(), "Expected expression.");
    }

    // ========================= utilities below =========================

    private boolean matchAny(TokenType... typesToMatch) {
        for (TokenType singleType : typesToMatch) {
            if (check(singleType)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private boolean check(TokenType expectedType) {
        if (isEnd()) {
            return false;
        }

        return peek().type == expectedType;
    }

    private Token peek() {
        return tokens.get(cur);
    }

    private Token advance() {
        if (!isEnd()) {
            ++cur;
        }
        return previous();
    }

    private boolean isEnd() {
        return tokens.get(cur).type == TokenType.EOF;
    }

    private Token previous() {
        if (cur == 0) {
            return tokens.get(cur);
        }
        return tokens.get(cur - 1);
    }

    private Token consume(TokenType expectedType, String errorMsg) {
        if (check(expectedType)) {
            return advance();
        }

        throw error(previous(), errorMsg);
    }

    private ParseError error(Token token, String errorMsg) {
        Lox.error(token.getLineNumber(), errorMsg);
        return new ParseError();
    }

    /**
     * Synchronize parser till next valid token (skipping all cascade failures).
     */
    private void synchronize() {

        advance();

        while (!isEnd()) {

            if (previous().type == TokenType.SEMICOLON) {
                return;
            }

            switch (peek().type) {
                case CLASS, FUN, VAR, FOR, IF, WHILE, PRINT, RETURN, LEFT_BRACE:
                    return;
            }

            advance();
        }
    }
}
