package org.max.crafting.interpreter.jlox.lexer;

public enum TokenType {

    // single char token
    LEFT_PAREN, RIGHT_PAREN,
    LEFT_BRACE, RIGHT_BRACE,
    COMMA, DOT, SEMICOLON, SLASH, STAR,
    MINUS, PLUS,

    // one or two character token
    BANG, BANG_EQUAL,
    EQUAL, EQUAL_EQUAL,
    LESS, LESS_EQUAL,
    GREATER, GREATER_EQUAL,

    // literals
    IDENTIFIER, STRING, NUMBER,

    // reserved keywords
    ADN, OR,
    IF, ELSE,
    WHILE, FOR,
    TRUE, FALSE, NIL,
    FUN, CLASS, THIS,
    PRINT,

    EOF
}
