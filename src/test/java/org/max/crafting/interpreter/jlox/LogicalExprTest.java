package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Tests related to logical expressions: ==, !=, <, <=, >, >=
 * Some double related corner cases also handled here, like NaN == NaN should return false.
 */
final class LogicalExprTest extends LoxBaseTest {

    @Test
    void equalsForStrings() {
        Lox.runScript("""
                              print("hello" == "hello");
                              print("Hello" == "hello");
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        true
                        false
                         """,
                output());
    }

    @Test
    void equalsForInt() {
        Lox.runScript("""
                              print((3*6) + 2 == 20);
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        true
                         """,
                output());
    }

    @Test
    void equalsForDouble() {
        Lox.runScript("""
                              print(30.0 / 5.0 == 6.0);
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        true
                         """,
                output());
    }

    @Test
    void singleNotEqLogicalExpression() {
        Lox.runScript("""
                              print((3*6) + 2 != 50);
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        true
                         """,
                output());
    }

    @Test
    void lessForIntegers() {
        Lox.runScript("""
                              print((3*3) + 2 < 20);
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        true
                         """,
                output());
    }

    @Test
    void lessEqualsForIntegers() {
        Lox.runScript("""
                              print(3+3 <= 20);
                              print(10+10 <= 20);
                              print(20 + 20 <= 20);
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        true
                        true
                        false
                         """,
                output());
    }

    @Test
    void greaterForIntegers() {
        Lox.runScript("""
                              print((3*3) + 2 > 20);
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        false
                         """,
                output());
    }

    @Test
    void greaterEqualsForIntegers() {
        Lox.runScript("""
                              print(20 + 20 >= 20);
                              print(10 + 10 >= 20);
                              print(1 + 1 >= 20);
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        true
                        true
                        false
                         """,
                output());
    }

    @Test
    void nilEqualsToNil() {
        Lox.runScript("""
                              print(nil == nil);
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        true
                         """,
                output());
    }

    @Test
    void nilShouldNotBeEqualToNumber() {
        Lox.runScript("""
                              print(nil == 133);
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        false
                         """,
                output());
    }

    // ==== few DOUBLE corner cases ====
    @Test
    void comparingNansShouldReturnFalse() {
        Lox.runScript("""
                              var res = 0.0/0.0 == 0.0/0.0;
                              print("res: " + res);
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        res: false
                         """,
                output());
    }
}
