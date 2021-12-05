package org.max.crafting.interpreter.jlox.lexer;

import java.util.HashMap;
import java.util.Map;

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
    AND, OR,
    IF, ELSE,
    WHILE, FOR,
    TRUE, FALSE, NIL,
    FUN, RETURN,
    CLASS, THIS, SUPER,
    PRINT, VAR,

    EOF;

    private static final Map<String, TokenType> KEYWORDS = new HashMap<>();
    static {
        KEYWORDS.put("and", AND);
        KEYWORDS.put("or", OR);
        KEYWORDS.put("if", IF);
        KEYWORDS.put("else", ELSE);
        KEYWORDS.put("while", WHILE);
        KEYWORDS.put("for", FOR);
        KEYWORDS.put("true", TRUE);
        KEYWORDS.put("false", FALSE);
        KEYWORDS.put("nil", NIL);
        KEYWORDS.put("fun", FUN);
        KEYWORDS.put("return", RETURN);
        KEYWORDS.put("class", CLASS);
        KEYWORDS.put("this", THIS);
        KEYWORDS.put("super", SUPER);
        KEYWORDS.put("print", PRINT);
        KEYWORDS.put("var", VAR);
    }

    public static TokenType findReservedKeyword(String name){
        return KEYWORDS.get(name);
    }


}
