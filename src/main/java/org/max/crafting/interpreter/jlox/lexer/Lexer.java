package org.max.crafting.interpreter.jlox.lexer;

import org.max.crafting.interpreter.jlox.Lox;
import org.max.crafting.interpreter.jlox.model.Token;
import org.max.crafting.interpreter.jlox.model.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Lexer {

    private final String source;
    private final List<Token> tokens;

    private int start = 0;
    private int cur = 0;
    private int lineNumber = 1;

    public Lexer(String source) {
        this.source = source;
        this.tokens = new ArrayList<>();
    }

    public List<Token> tokenize() {

        while (!isEof()) {
            start = cur;
            scanToken();
        }

        addToken(TokenType.EOF);
        return tokens;
    }

    private boolean isEof() {
        return cur >= source.length();
    }

    private void scanToken() {
        char ch = advance();

        switch (ch) {

            // ignore space, tab, carriage return and new line
            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                ++lineNumber;
                break;

            // 1 char only tokens
            case '(':
                addToken(TokenType.LEFT_PAREN);
                break;
            case ')':
                addToken(TokenType.RIGHT_PAREN);
                break;
            case '{':
                addToken(TokenType.LEFT_BRACE);
                break;
            case '}':
                addToken(TokenType.RIGHT_BRACE);
                break;
            case ',':
                addToken(TokenType.COMMA);
                break;
            case '.':
                addToken(TokenType.DOT);
                break;
            case '-':
                addToken(TokenType.MINUS);
                break;
            case '+':
                addToken(TokenType.PLUS);
                break;
            case ';':
                addToken(TokenType.SEMICOLON);
                break;
            case '*':
                addToken(TokenType.STAR);
                break;

            // 1 or 2 chars tokens
            case '!':
                addToken(matchNext('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
                break;
            case '=':
                addToken(matchNext('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
                break;
            case '<':
                addToken(matchNext('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
                break;
            case '>':
                addToken(matchNext('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
                break;

            // comment or slash
            case '/':
                if (matchNext('/')) {
                    skipLine();
                }
                else {
                    addToken(TokenType.SLASH);
                }
                break;

            // string literal
            case '"':
                readString();
                break;

            default:
                // number
                if (isDigit(ch)) {
                    readNumber();
                }
                // identifier or reserved word
                else if (isAlpha(ch)) {
                    readIdentifier();
                }
                // handle lexical errors here
                else {
                    Lox.error(lineNumber, "Unexpected character " + ch);
                }
                break;
        }

    }

    // read string literal
    private void readString() {

        final int initialLineNumber = lineNumber;
        char lastCh = peek();

        while (!isEof()) {
            // we support multi-line strings, so we need to handle '\n' here too
            if (lastCh == '\n') {
                ++lineNumber;
            }
            else if (lastCh == '"') {
                break;
            }

            lastCh = advance();
        }

        if (lastCh == '\0') {
            Lox.error(initialLineNumber, "Unterminated string found");
            return;
        }

        String value = source.substring(start + 1, cur - 1);
        addToken(TokenType.STRING, value);
    }

    // read number as double value
    private void readNumber() {

        // read first part of number
        while (isDigit(peek())) {
            advance();
        }

        char ch = peek();
        boolean hasFraction = false;

        // read fractional part if any
        if (ch == '.' && isDigit(peekNext())) {

            hasFraction = true;
            advance();

            while (isDigit(peek())) {
                advance();
            }
        }
        String numberStr = source.substring(start, cur);

        Object val = null;

        if( hasFraction ){
            val = Double.parseDouble(numberStr) ;
        }
        else {
            val =  Integer.parseInt(numberStr);
        }

        addToken(TokenType.NUMBER, val);
    }

    //read identifier or reserved keyword
    private void readIdentifier() {
        while (isAlphaNumeric(peek())) {
            advance();
        }

        String identifierName = source.substring(start, cur);

        TokenType keywordType = TokenType.findReservedKeyword(identifierName);
        if (keywordType != null) {
            addToken(keywordType);
        }
        else {
            addToken(TokenType.IDENTIFIER, identifierName);
        }
    }

    private boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    private boolean isAlpha(char ch) {
        return ch == '_' ||
                (ch >= 'a' && ch <= 'z') ||
                (ch >= 'A' && ch <= 'Z');
    }

    private boolean isAlphaNumeric(char ch) {
        return isAlpha(ch) || isDigit(ch);
    }

    private void addToken(TokenType type) {
        tokens.add(new Token(type, null, null, lineNumber));
    }

    private void addToken(TokenType type, Object value) {
        tokens.add(new Token(type, null, value, lineNumber));
    }

    private void skipLine() {
        while (!isEof()) {
            char nextCh = advance();
            if (nextCh == '\n') {
                ++lineNumber;
                break;
            }
        }
    }

    private char advance() {
        char curCh = source.charAt(cur);
        ++cur;
        return curCh;
    }

    private boolean matchNext(char expectedNextCh) {
        if (isEof()) {
            return false;
        }

        if (source.charAt(cur) != expectedNextCh) {
            return false;
        }

        ++cur;
        return true;
    }

    // return cur character and do NOT advance cursor
    private char peek() {
        if (cur >= source.length()) {
            return '\0';
        }
        return source.charAt(cur);
    }

    // return cur+1 character and do NOT advance cursor
    private char peekNext() {
        if ((cur + 1) >= source.length()) {
            return '\0';
        }
        return source.charAt(cur + 1);
    }
}
