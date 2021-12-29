package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests related to plus expression: int + int, double + double, int + double etc.
 * String concatenation handled separately.
 */
final class PlusExpressionTest extends LoxBaseTest {

    @Test
    void plusForIntegers() {
        Lox.runScript("""
                              var res = 10 + 20 + 30;
                              print res;
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        60
                         """,
                output());
    }

    @Test
    void plusForFloatingNumbers() {
        Lox.runScript("""
                              var res = 10.2 + 20.2 + 30.4;
                              print res;
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        60.8
                         """,
                output());
    }

    @Test
    void plusForIntegersAndFloats() {
        Lox.runScript("""
                              var res = 100 + 10.4 + 20.4 + 200 + 30.4;
                              print res;
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        361.2
                         """,
                output());
    }

    @Test
    void plusForTwoBooleansShouldFail() {
        Lox.runScript("""
                              var res = true + false;
                              print res;
                              """);

        assertTrue(Lox.hasError(), "Expected error here");
        assertEquals(
                """
                        [line 1]: Both operands must be numbers or one should be a string.
                         """,
                errorOutput());
    }

    @Test
    void plusForBooleanAndIntegerShouldFail() {
        Lox.runScript("""
                              var res = true + 133;
                              print res;
                              """);

        assertTrue(Lox.hasError(), "Expected error here");
        assertEquals(
                """
                        [line 1]: Both operands must be numbers or one should be a string.
                         """,
                errorOutput());
    }
}
