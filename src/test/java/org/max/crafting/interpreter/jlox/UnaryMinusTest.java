package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test related to unary minus.
 */
final class UnaryMinusTest extends LoxBaseTest {

    @Test
    void unaryMinusForInt() {
        Lox.runScript("""
                              var res = 2 * (6 / -3);
                              print(res);
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        -4
                         """,
                output());
    }

    @Test
    void unaryMinusForDoubleAndInt() {
        Lox.runScript("""
                              var res = -20.2 * (6 / -3);
                              print(res);
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        40.4
                         """,
                output());
    }

    @Test
    void negateStringShouldFail() {
        Lox.runScript("""
                              var res = -"hello, world";
                              print(res);
                              """);

        assertTrue(Lox.hasError(), "Expected error here");
        assertEquals(
                """
                        [line 1]: Operand must be a number.
                         """,
                errorOutput());
    }
}
