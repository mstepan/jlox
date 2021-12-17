package org.max.crafting.interpreter.jlox.parser;

import org.max.crafting.interpreter.jlox.Lox;
import org.max.crafting.interpreter.jlox.ast.BinaryExpression;
import org.max.crafting.interpreter.jlox.ast.Expression;
import org.max.crafting.interpreter.jlox.ast.Grouping;
import org.max.crafting.interpreter.jlox.ast.Literal;
import org.max.crafting.interpreter.jlox.ast.UnaryExpression;
import org.max.crafting.interpreter.jlox.model.Token;
import org.max.crafting.interpreter.jlox.model.TokenType;

import java.util.List;

/**
 * Use recursive descent parser technique, so grammar should be NON left-recursive (see main-grammar.txt).
 */
@SuppressWarnings("LoopStatementThatDoesntLoop")
public class RecursiveDescentParser {

    private final List<Token> tokens;
    private int cur = 0;

    public RecursiveDescentParser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Expression parse() {
        try {
            return expression();
        }
        catch (ParseError parseError) {
            parseError.printStackTrace();
        }
        return null;
    }

    /**
     * expression = equality
     */
    private Expression expression() {
        return equality();
    }

    /**
     * equality = comparison  (("==" | "!=") comparison)*
     */
    private Expression equality() {
        Expression expr = comparison();

        while (matchAny(TokenType.EQUAL_EQUAL, TokenType.BANG_EQUAL)) {
            Token op = previous();
            Expression rightExp = comparison();
            return new BinaryExpression(expr, op, rightExp);
        }

        return expr;
    }

    /**
     * comparison = term ((">" | "<=" | "<" | "<=") term)*
     */
    private Expression comparison() {
        Expression expr = term();

        while (matchAny(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
            Token op = previous();
            Expression rightExp = term();
            return new BinaryExpression(expr, op, rightExp);
        }

        return expr;
    }

    /**
     * term = factor (("-" | "+") factor)*
     */
    private Expression term() {

        Expression expr = factor();

        while (matchAny(TokenType.MINUS, TokenType.PLUS)) {
            Token op = previous();
            Expression rightSide = factor();
            return new BinaryExpression(expr, op, rightSide);
        }

        return expr;
    }

    /**
     * factor = unary (("*" | "/" ) unary)*
     */
    private Expression factor() {
        Expression expr = unary();

        while (matchAny(TokenType.STAR, TokenType.SLASH)) {
            Token op = previous();
            Expression rightSide = unary();
            return new BinaryExpression(expr, op, rightSide);
        }

        return expr;
    }

    /**
     * unary = ("!" | "-") unary | primary
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
     * primary = STRING | NUMBER | "true" | "false" | nil | "(" expression ")"
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

        throw error(peek(), "Expected expression.");
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

        return peek().getType() == expectedType;
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
        return tokens.get(cur).getType() == TokenType.EOF;
    }

    private Token previous() {
        return tokens.get(cur - 1);
    }

    private Token consume(TokenType expectedType, String errorMsg) {
        if (check(expectedType)) {
            return advance();
        }

        throw error(peek(), errorMsg);
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
            if (previous().getType() == TokenType.SEMICOLON) {
                return;
            }

            switch (peek().getType()) {
                case CLASS, FUN, VAR, FOR, IF, WHILE, PRINT, RETURN:
                    return;
            }

            advance();
        }
    }
}
