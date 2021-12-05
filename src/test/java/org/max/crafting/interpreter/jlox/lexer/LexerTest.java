package org.max.crafting.interpreter.jlox.lexer;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class LexerTest {


    @Test
    void tokenizeSimpleFile() throws IOException {

        String source = Files.readString(Paths.get("src/test/resources/program1.jlox"),
                                         Charset.defaultCharset());
        Lexer lexer = new Lexer(source);

        List<Token> tokens = lexer.tokenize();

        final int tokensCount = 36;

        assertThat(tokens).
                isNotEmpty().
                hasSize(tokensCount).
                containsExactly(
                        //var x = 10.33;    line 1
                        var(1), identifier("x", 1), eq(1), number(10.33, 1), semicolon(1),

                        //var y = 20;   line 2
                        var(2), identifier("y", 2), eq(2), number(20, 2), semicolon(2),

                        //var str = "hello, world"; line 6
                        var(6), identifier("str", 6), eq(6), string("hello, world", 6), semicolon(6),

                        // var z = x;   line 8
                        var(8), identifier("z", 8), eq(8), identifier("x", 8), semicolon(8),

                        //if(y > z){    line 10
                        ifKeyword(10), leftParen(10), identifier("y", 10), greater(10), identifier("z", 10),
                        rightParen(10), leftBrace(10),

                        // z = y;   line 11
                        identifier("z", 11), eq(11), identifier("y", 11), semicolon(11),

                        // }    line 12
                        rightBrace(12),

                        //print z;  line 13
                        print(13), identifier("z", 13), semicolon(13),

                        // line 14
                        eof(14)
                );
    }

    private static Token identifier(String name, int lineNumber) {
        return new Token(TokenType.IDENTIFIER, null, name, lineNumber);
    }

    private static Token var(int lineNumber) {
        return new Token(TokenType.VAR, null, null, lineNumber);
    }

    private static Token leftBrace(int lineNumber) {
        return new Token(TokenType.LEFT_BRACE, null, null, lineNumber);
    }

    private static Token rightBrace(int lineNumber) {
        return new Token(TokenType.RIGHT_BRACE, null, null, lineNumber);
    }

    private static Token leftParen(int lineNumber) {
        return new Token(TokenType.LEFT_PAREN, null, null, lineNumber);
    }

    private static Token rightParen(int lineNumber) {
        return new Token(TokenType.RIGHT_PAREN, null, null, lineNumber);
    }

    private static Token eq(int lineNumber) {
        return new Token(TokenType.EQUAL, null, null, lineNumber);
    }

    private static Token greater(int lineNumber) {
        return new Token(TokenType.GREATER, null, null, lineNumber);
    }

    private static Token number(double value, int lineNumber) {
        return new Token(TokenType.NUMBER, null, value, lineNumber);
    }

    private static Token string(String value, int lineNumber) {
        return new Token(TokenType.STRING, null, value, lineNumber);
    }

    private static Token semicolon(int lineNumber) {
        return new Token(TokenType.SEMICOLON, null, null, lineNumber);
    }

    private static Token ifKeyword(int lineNumber) {
        return new Token(TokenType.IF, null, null, lineNumber);
    }

    private static Token print(int lineNumber) {
        return new Token(TokenType.PRINT, null, null, lineNumber);
    }

    private static Token eof(int lineNumber) {
        return new Token(TokenType.EOF, null, null, lineNumber);
    }
}
