package org.max.crafting.interpreter.jlox.parser;

import org.max.crafting.interpreter.jlox.Lox;
import org.max.crafting.interpreter.jlox.ast.Assignment;
import org.max.crafting.interpreter.jlox.ast.BinaryExpression;
import org.max.crafting.interpreter.jlox.ast.Expression;
import org.max.crafting.interpreter.jlox.ast.ExpressionStmt;
import org.max.crafting.interpreter.jlox.ast.Grouping;
import org.max.crafting.interpreter.jlox.ast.Literal;
import org.max.crafting.interpreter.jlox.ast.PrintStmt;
import org.max.crafting.interpreter.jlox.ast.Stmt;
import org.max.crafting.interpreter.jlox.ast.UnaryExpression;
import org.max.crafting.interpreter.jlox.ast.VarStmt;
import org.max.crafting.interpreter.jlox.ast.VariableExpression;
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
        try {
            while (!isEnd()) {
                statements.add(declaration());
            }
        }
        catch (ParseError error) {
            System.err.println(error.getMessage());
        }
        return statements;
    }

    public Expression eval() {
        try {
            return expression();
        }
        catch (ParseError error) {
            System.err.println(error.getMessage());
        }
        return null;
    }

    /**
     * declaration -> varDeclaration | statement
     */
    private Stmt declaration() {
        try {
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
     * statement -> exprStmt | printStmt
     */
    private Stmt statement() {
        if (matchAny(TokenType.PRINT)) {
            return printStatement();
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
     * exprStmt -> expression ";"
     */
    private Stmt expressionStatement() {
        Expression expr = expression();
        consume(TokenType.SEMICOLON, "Expected ';' after expression.");
        return new ExpressionStmt(expr);
    }

    /**
     * expression -> assignment
     */
    private Expression expression() {
        return assignment();
    }

    /**
     * assignment -> IDENTIFIER "=" assignment | equality
     */
    private Expression assignment() {

        Expression left = equality();

        if (matchAny(TokenType.EQUAL)) {
            Token equalsToken = previous();
            Expression right = assignment();

            if (left instanceof VariableExpression) {
                Token name = ((VariableExpression) left).name;
                return new Assignment(name, right);
            }

            throw error(equalsToken, "Invalid assignment target.");
        }
        return left;
    }

    /**
     * equality -> comparison  (("==" | "!=") comparison)*
     */
    private Expression equality() {
        Expression expr = comparison();

        while (matchAny(TokenType.EQUAL_EQUAL, TokenType.BANG_EQUAL)) {
            Token op = previous();
            Expression rightExp = comparison();
            expr = new BinaryExpression(expr, op, rightExp);
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
            expr = new BinaryExpression(expr, op, rightExp);
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
            expr = new BinaryExpression(expr, op, rightSide);
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
            expr = new BinaryExpression(expr, op, rightSide);
        }

        return expr;
    }

    /**
     * unary -> ("!" | "-") unary | primary
     */
    private Expression unary() {
        if (matchAny(TokenType.BANG, TokenType.MINUS)) {
            Token op = previous();
            Expression right = unary();
            return new UnaryExpression(op, right);
        }

        return primary();
    }

    /**
     * primary -> STRING | NUMBER | "true" | "false" | nil | "(" expression ")" | IDENTIFIER
     */
    private Expression primary() {
        if (matchAny(TokenType.FALSE)) {
            return new Literal(false);
        }

        if (matchAny(TokenType.TRUE)) {
            return new Literal(true);
        }

        if (matchAny(TokenType.NIL)) {
            return new Literal(null);
        }

        if (matchAny(TokenType.NUMBER, TokenType.STRING)) {
            return new Literal(previous().getLiteral());
        }

        if (matchAny(TokenType.LEFT_PAREN)) {
            Expression expr = expression();
            consume(TokenType.RIGHT_PAREN, "')' expected");
            return new Grouping(expr);
        }

        if (matchAny(TokenType.IDENTIFIER)) {
            return new VariableExpression(previous());
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

        while (!isEnd()) {
            if (previous().type == TokenType.SEMICOLON) {
                return;
            }

            switch (peek().type) {
                case CLASS, FUN, VAR, FOR, IF, WHILE, PRINT, RETURN:
                    return;
            }

            advance();
        }
    }
}
