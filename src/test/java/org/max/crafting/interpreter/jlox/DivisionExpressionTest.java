package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests related to division expression.
 */
final class DivisionExpressionTest extends LoxBaseTest {

    // ==== INTEGER ====

    @Test
    void intDivision() {
        Lox.runScript("""
                              print 10/2; 
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");

        assertEquals(
                """
                        5
                         """,
                output());
    }

    @Test
    void intDivisionWithRemaining() {
        Lox.runScript("""
                              print 10/3; 
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");

        assertEquals(
                """
                        3
                         """,
                output());
    }

    @Test
    void intDivisionByZeroShouldFail() {
        Lox.runScript("""
                              var x = 10/0;
                              """);

        assertTrue(Lox.hasError(), "Expected error here");

        assertEquals(
                """
                        [line 1]: Integer division by zero.
                         """,
                errorOutput());
    }

    //==== DOUBLE ====

    @Test
    void doubleDivision() {
        Lox.runScript("""
                              print 10.0/2.0; 
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");

        assertEquals(
                """
                        5.0
                         """,
                output());
    }

    @Test
    void doubleDivisionWithRemaining() {
        Lox.runScript("""
                              print 10.0/4.0; 
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");

        assertEquals(
                """
                        2.5
                         """,
                output());
    }

    // ==== MIXED type division (int, double) ====
    @Test
    void mixedDivisionDoubleByInt() {
        Lox.runScript("""
                              print 30.0/5; 
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");

        assertEquals(
                """
                        6.0
                         """,
                output());
    }

    @Test
    void mixedDivisionIntByDouble() {
        Lox.runScript("""
                              print 20/4.0; 
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");

        assertEquals(
                """
                        5.0
                         """,
                output());
    }

}
