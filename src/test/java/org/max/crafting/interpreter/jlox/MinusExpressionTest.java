package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests related to minus expression.
 */
final class MinusExpressionTest extends LoxBaseTest {

    @Test
    void minusForIntegers() {
        Lox.runScript("""
                              print(10 - 20 - 30);
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        -40
                         """,
                output());
    }

    @Test
    void minusForDoubles() {
        Lox.runScript("""
                              print(30.50 - 10.25);
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        20.25
                         """,
                output());
    }

    @Test
    void minusForTwoBooleansShouldFail() {
        Lox.runScript("""
                              print(true - false);
                              """);

        assertTrue(Lox.hasError(), "Expected error here");
        assertEquals(
                """
                        [line 1]: Both operands must be numbers.
                         """,
                errorOutput());
    }

    @Test
    void minusForBooleanAndIntegerShouldFail() {
        Lox.runScript("""
                              print(true - 133);
                              """);

        assertTrue(Lox.hasError(), "Expected error here");
        assertEquals(
                """
                        [line 1]: Both operands must be numbers.
                         """,
                errorOutput());
    }
}
