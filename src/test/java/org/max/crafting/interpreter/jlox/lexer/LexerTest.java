package org.max.crafting.interpreter.jlox.lexer;

import org.junit.jupiter.api.Test;
import org.max.crafting.interpreter.jlox.model.Token;
import org.max.crafting.interpreter.jlox.model.TokenType;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

final class LexerTest {

    @Test
    void tokenizeNumberExpression()  {
        final Lexer lexer = new Lexer("1+2+3");
        List<Token> tokens = lexer.tokenize();
        assertThat(tokens).hasSize(6);
    }

    @Test
    void tokenizeString()  {
        final Lexer lexer = new Lexer("\"hello\"");
        List<Token> tokens = lexer.tokenize();
        assertThat(tokens).hasSize(2);
    }

    @Test
    void tokenizeUnterminatedString()  {
        final Lexer lexer = new Lexer("\"hello");
        List<Token> tokens = lexer.tokenize();
        assertThat(tokens).hasSize(2);
    }

    @Test
    void tokenizeSimpleFile() throws IOException {

        String source = Files.readString(Paths.get("src/test/resources/program1.jlox"),
                                         Charset.defaultCharset());
        Lexer lexer = new Lexer(source);

        List<Token> tokens = lexer.tokenize();

        final int tokensCount = 39;

        Token[] expectedTokens = new TokenBuilder().

                //var x = 10.33;    line 1
                        var().identifier("x").eq().number(10.33).semicolon().
                        newLine().

                //var y = 20;   line 2
                        var().identifier("y").eq().number(20).semicolon().
                        newLine().
                        newLine().
                        newLine().
                        newLine().

                //var str = "hello, world"; line 6
                        var().identifier("str").eq().string("hello, world").semicolon().
                        newLine().
                        newLine().

                // var z = x;   line 8
                        var().identifier("z").eq().identifier("x").semicolon().
                        newLine().
                        newLine().

                //if(y > z){    line 10
                        ifKeyword().leftParen().identifier("y").greater().identifier("z").rightParen().leftBrace().
                        newLine().

                // z = y;   line 11
                        identifier("z").eq().identifier("y").semicolon().
                        newLine().

                // }    line 12
                        rightBrace().
                        newLine().

                //print z;  line 13
                        print().identifier("z").semicolon().
                        newLine().
                        newLine().

                // var printMe; line 15
                        var().identifier("printMe").semicolon().
                        newLine().

                // line 16
                        eof().

                        build();

        assertThat(tokens).
                isNotEmpty().
                hasSize(tokensCount).
                containsExactly(expectedTokens);
    }

    public static final class TokenBuilder {

        final List<Token> tokens = new ArrayList<>();
        int lineNumber = 1;

        TokenBuilder var() {
            tokens.add(new Token(TokenType.VAR, null, null, lineNumber));
            return this;
        }

        TokenBuilder ifKeyword() {
            tokens.add(new Token(TokenType.IF, null, null, lineNumber));
            return this;
        }

        TokenBuilder identifier(String name) {
            tokens.add(new Token(TokenType.IDENTIFIER, null, name, lineNumber));
            return this;
        }

        TokenBuilder eq() {
            tokens.add(new Token(TokenType.EQUAL, null, null, lineNumber));
            return this;
        }

        TokenBuilder number(int value) {
            tokens.add(new Token(TokenType.NUMBER, null, value, lineNumber));
            return this;
        }

        TokenBuilder number(double value) {
            tokens.add(new Token(TokenType.NUMBER, null, value, lineNumber));
            return this;
        }

        TokenBuilder string(String value) {
            tokens.add(new Token(TokenType.STRING, null, value, lineNumber));
            return this;
        }

        TokenBuilder semicolon() {
            tokens.add(new Token(TokenType.SEMICOLON, null, null, lineNumber));
            return this;
        }

        TokenBuilder leftParen() {
            tokens.add(new Token(TokenType.LEFT_PAREN, null, null, lineNumber));
            return this;
        }

        TokenBuilder rightParen() {
            tokens.add(new Token(TokenType.RIGHT_PAREN, null, null, lineNumber));
            return this;
        }

        TokenBuilder leftBrace() {
            tokens.add(new Token(TokenType.LEFT_BRACE, null, null, lineNumber));
            return this;
        }

        TokenBuilder rightBrace() {
            tokens.add(new Token(TokenType.RIGHT_BRACE, null, null, lineNumber));
            return this;
        }

        TokenBuilder greater() {
            tokens.add(new Token(TokenType.GREATER, null, null, lineNumber));
            return this;
        }

        TokenBuilder print() {
            tokens.add(new Token(TokenType.PRINT, null, null, lineNumber));
            return this;
        }

        TokenBuilder eof() {
            tokens.add(new Token(TokenType.EOF, null, null, lineNumber));
            return this;
        }

        TokenBuilder newLine() {
            ++lineNumber;
            return this;
        }

        Token[] build() {
            return tokens.toArray(new Token[0]);
        }
    }
}
