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

        assertThat(tokens).
                isNotEmpty().
                hasSize(11).
                containsExactly(
                        identifier("var", 1), identifier("x", 1), eq(1), number(10.33, 1), semicolon(1),
                        identifier("var", 2), identifier("y", 2), eq(2), number(20, 2), semicolon(2),
                        eof(6)
                );
    }

    private static Token identifier(String name, int lineNumber) {
        return new Token(TokenType.IDENTIFIER, null, name, lineNumber);
    }

    private static Token eq(int lineNumber) {
        return new Token(TokenType.EQUAL, null, null, lineNumber);
    }

    private static Token number(double value, int lineNumber) {
        return new Token(TokenType.NUMBER, null, value, lineNumber);
    }

    private static Token semicolon(int lineNumber) {
        return new Token(TokenType.SEMICOLON, null, null, lineNumber);
    }

    private static Token eof(int lineNumber) {
        return new Token(TokenType.EOF, null, null, lineNumber);
    }
}
