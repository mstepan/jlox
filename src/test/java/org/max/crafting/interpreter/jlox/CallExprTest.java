package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class CallExprTest extends LoxBaseTest {

    @Test
    void callNativeFunction() {
        Lox.runScript("""
                              var x = 10;
                              var z = max(x, 133);
                              print z;
                              """);
        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        133
                        """,
                output());
    }

    @Test
    void callWithoutLastParenShouldFail() {
        Lox.runScript("""
                              max(10, 20;
                              """);

        assertTrue(Lox.hasError(), "Expected error here");
        assertEquals(
                """
                        [line 1] Expected ')' after arguments list.
                        """,
                errorOutput());
    }
}
