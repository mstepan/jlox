package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class LoxTest extends LoxBaseTest {

    @Test
    void runNormalScript() {

        String script = """
                // some dummy unused variables declaration
                var x = 10;
                var y = 20;
                var z;
                            
                print "x = " + x;
                print "y = " + y;
                print "Uninitialized variable is set to: " + z;
                            
                // assignment var to var
                x = y;
                            
                print "x = " + x;
                print "y = " + y;
                            
                // assigment var to expression
                x = (2*3) + 4;
                            
                print "x = " + x;
                            
                // radius for a circle as int
                var r = 10;
                            
                // PI constant as a double
                var pi = 3.14;
                            
                // printing some message
                print "calculating area for circle with radius: " + r + " m";
                            
                var area = pi * r * r;
                            
                print "area: " + area + " m^2";

                """;

        Lox.runScript(script);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        x = 10
                        y = 20
                        Uninitialized variable is set to: nil
                        x = 20
                        y = 20
                        x = 10
                        calculating area for circle with radius: 10 m
                        area: 314.0 m^2
                        """,
                output());
    }

    //==== PLUS operator ====

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

    // ==== DOUBLE ====

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

    // ==== DIVISION ====

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

    // ==== Complex expression ====

    @Test
    void singleNumericalExpression() {
        Lox.runScript("""
                              var res = (3*6) + 2;
                              print res;
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        20
                         """,
                output());
    }

    // === LOGICAL expressions ====
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

    // ==== UNARY minus ====
    @Test
    void unaryMinusForInt() {
        Lox.runScript("""
                              var res = 2 * (6 / -3);
                              print res;
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
                              print res;
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
                              print res;
                              """);

        assertTrue(Lox.hasError(), "Expected error here");
        assertEquals(
                """
                        [line 1]: Operand must be a number.
                         """,
                errorOutput());
    }

    // ==== GROUPING ====

    @Test
    void complexExpressionWithGroupingAndUnaryMinus() {
        Lox.runScript("""
                              var res = ((10+20) * 33.24) - 177.45 + (-20);
                              print res;
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        799.75
                         """,
                output());
    }

    @Test
    void groupingWithoutMatchingParenthesesShouldFail() {
        Lox.runScript("""
                              var res = (3*6 + 2 != 50;
                              print res;
                              """);

        assertTrue(Lox.hasError(), "Expected error here");
        assertEquals(
                """
                        [line 1] Matching ')' expected.
                         """,
                errorOutput());
    }

}
