package org.max.crafting.interpreter.jlox.model;

import java.util.HashMap;
import java.util.Map;

public enum TokenType {

    // single char token
    LEFT_PAREN("("), RIGHT_PAREN(")"),
    LEFT_BRACE("{"), RIGHT_BRACE("}"),
    COMMA(","), DOT("."), SEMICOLON(";"), SLASH("/"), STAR("*"),
    MINUS("-"), PLUS("+"),

    // one or two character token
    BANG("!"), BANG_EQUAL("!="),
    EQUAL("="), EQUAL_EQUAL("=="),
    LESS("<"), LESS_EQUAL("<="),
    GREATER(">"), GREATER_EQUAL(">="),

    // literals
    IDENTIFIER("id"), STRING("string"), NUMBER("number"),

    // reserved keywords
    AND("and"), OR("or"),
    IF("if"), ELSE("else"),
    WHILE("while"), FOR("for"),
    TRUE("true"), FALSE("false"), NIL("nil"),
    FUN("fun"), RETURN("return"),
    CLASS("class"), THIS("this"), SUPER("super"),
    PRINT("print"), VAR("var"),

    EOF("eof");

    private final String name;

    TokenType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

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
