package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Tests related to logical expressions: ==, !=, <, <=, >, >=
 * Some double related corner cases also handled here, like NaN == NaN should return false.
 */
final class LogicalExpressionTest extends LoxBaseTest {

    @Test
    void singleEqEqLogicalExpression() {
        Lox.runScript("""
                              var res = (3*6) + 2 == 20;
                              print res;
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
                              var res = (3*6) + 2 != 50;
                              print res;
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        true
                         """,
                output());
    }

    @Test
    void lessForInts() {
        Lox.runScript("""
                              var res = (3*3) + 2 < 20;
                              print res;
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        true
                         """,
                output());
    }

    @Test
    void greaterForInts() {
        Lox.runScript("""
                              var res = (3*3) + 2 > 20;
                              print res;
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
                              print "res: " + res;
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        res: false
                         """,
                output());
    }
}
