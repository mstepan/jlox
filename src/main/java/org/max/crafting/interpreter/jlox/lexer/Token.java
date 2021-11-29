package org.max.crafting.interpreter.jlox.lexer;

public class Token {

    final TokenType type;
    final String lexeme;
    final Object literal;
    final int lineNumber;

    public Token(TokenType type, String lexeme, Object literal, int lineNumber) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.lineNumber = lineNumber;
    }

    @Override
    public String toString() {
        return type + " " + (lexeme == null ? "" : lexeme) + " " + (literal == null ? "" : literal);
    }
}
