package org.max.crafting.interpreter.jlox.model;

import java.util.Objects;

public class Token {

    public final TokenType type;
    public final String lexeme;
    public final Object literal;
    public final int lineNumber;

    public Token(TokenType type, String lexeme, Object literal, int lineNumber) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.lineNumber = lineNumber;
    }

    public Object getLiteral() {
        return literal;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Token)) {
            return false;
        }
        Token that = (Token) obj;
        if (lineNumber != that.lineNumber) {
            return false;
        }
        if (type != that.type) {
            return false;
        }
        if (!Objects.equals(lexeme, that.lexeme)) {
            return false;
        }
        return Objects.equals(literal, that.literal);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + Objects.hashCode(lexeme);
        result = 31 * result + Objects.hashCode(literal);
        result = 31 * result + Integer.hashCode(lineNumber);
        return result;
    }

    @Override
    public String toString() {
        return type + " " + (lexeme == null ? "" : lexeme) + " " + (literal == null ? "" : literal) + ", line: " + lineNumber;
    }
}
