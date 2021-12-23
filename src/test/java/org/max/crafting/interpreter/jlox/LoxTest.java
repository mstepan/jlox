package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class LoxTest {

    //==== Concatenation ====
    @Test
    void stringConcatenation() {
        Lox.run("\"hello\" + \"world\"");
        assertFalse(Lox.hasError());
        assertEquals("helloworld", Lox.result);
    }

    @Test
    void stringAndNumberConcatenation() {
        Lox.run("\"hello\" + 133");
        assertFalse(Lox.hasError());
        assertEquals("hello133", Lox.result);
    }

    @Test
    void stringAndBooleanConcatenation() {
        Lox.run("\"hello\" + true");
        assertFalse(Lox.hasError());
        assertEquals("hellotrue", Lox.result);
    }

    @Test
    void stringAndNilConcatenation() {
        Lox.run("\"hello\" + nil");
        assertFalse(Lox.hasError());
        assertEquals("hellonil", Lox.result);
    }

    @Test
    void numberAndStringConcatenation() {
        Lox.run("133 + \"str-123\"");
        assertFalse(Lox.hasError());
        assertEquals("133str-123", Lox.result);
    }

    @Test
    void booleanAndStringConcatenation() {
        Lox.run("true + \"str-123\"");
        assertFalse(Lox.hasError());
        assertEquals("truestr-123", Lox.result);
    }

    //==== PLUS operator ====

    @Test
    void plusForIntegers() {
        Lox.run("10+20");

        assertFalse(Lox.hasError());
        assertEquals("30", Lox.result);
    }

    @Test
    void plusForFloatingNumbers() {
        Lox.run("10.23 + 20.45");

        assertFalse(Lox.hasError());
        assertEquals("30.68", Lox.result);
    }

    @Test
    void multiplePlusOperators() {
        Lox.run("1+2+3+4+5");

        assertFalse(Lox.hasError());
        assertEquals("15", Lox.result);
    }

    @Test
    void plusForTwoBooleansShouldFail() {
        Lox.run("true + false");
        assertTrue(Lox.hasError());
    }

    // ==== expressions ====
    @Test
    void singleNumericalExpression() {
        Lox.run("(3*6) + 2");
        assertFalse(Lox.hasError());
        assertEquals("20", Lox.result);
    }

    @Test
    void singleEqEqLogicalExpression() {
        Lox.run("(3*6) + 2 == 20");
        assertFalse(Lox.hasError());
        assertEquals("true", Lox.result);
    }

    @Test
    void singleBangEqLogicalExpression() {
        Lox.run("(3*6) + 2 != 50");
        assertFalse(Lox.hasError());
        assertEquals("true", Lox.result);
    }

    @Test
    void commaSeparatedExpressions() {
        Lox.run("(3*6) + 2 != 50, 2 * 4 == 8");
        assertFalse(Lox.hasError());
        assertEquals("true", Lox.result);
    }

    /**
     * This test case works just check that we use Double.equals(...) for '==' comparison,
     * which in java returns 'true' for NaN.equals(NaN).
     */
    @Test
    void comparingNans() {
        Lox.run("0/0 == 0/0");

        assertFalse(Lox.hasError());
        assertEquals("true", Lox.result);
    }

    @Test
    void expressionsWithUnaryMinus() {
        Lox.run("2 * (6 / -3)");

        assertFalse(Lox.hasError());
        assertEquals("-4", Lox.result);
    }

    @Test
    void complexExpressionWithGroupingAndUnaryMinus() {
        Lox.run("((10+20) * 33.24) - 177.45 + (-20)");

        assertFalse(Lox.hasError());
        assertEquals("799.75", Lox.result);
    }

    @Test
    void groupingWithoutMatchingParenthesesShouldFail() {
        Lox.run("(3*6 + 2 != 50");

        assertTrue(Lox.hasError());
        assertEquals("[line 1] Error: ')' expected", Lox.lastErrorMsg);
    }

    @Test
    void negateStringShouldFail() {
        Lox.run("2 * (6 / -\"hello, world\")");

        assertTrue(Lox.hasError());
        assertEquals("[line 1]: Operand must be a number.", Lox.lastErrorMsg);
    }
}
