package org.max.crafting.interpreter.jlox.lexer;

import org.max.crafting.interpreter.jlox.Lox;

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

        tokens.add(new Token(TokenType.EOF, null, null, lineNumber));

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

            // handle lexical errors here
            default:
                Lox.error(lineNumber, "Unexpected character " + ch);
                break;
        }

    }

    private void readString() {

        final int initialLineNumber = lineNumber;

        while (!isEof()) {

            char ch = advance();

            // we support multi-line strings, so we need to handle '\n' here too
            if (ch == '\n') {
                ++lineNumber;
            }
            else if (ch == '"') {
                break;
            }
        }

        if (isEof()) {
            Lox.error(initialLineNumber, "Unterminated string found");
            return;
        }

        String value = source.substring(start + 1, cur - 1);
        addToken(TokenType.STRING, value);
    }

    private void addToken(TokenType type) {
        tokens.add(new Token(type, null, null, lineNumber));
    }

    private void addToken(TokenType type, String value) {
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

    private char advance() {
        char curCh = source.charAt(cur);
        ++cur;
        return curCh;
    }
}
