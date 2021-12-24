package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class LoxTest {

    @Test
    void divisionByZeroShouldThrowException() {
        Lox.eval("10/0");
        assertTrue(Lox.hasError());
    }

    @Test
    void comparingNansShouldReturnFalse() {
        Object res = Lox.eval("0.0/0.0 == 0.0/0.0");

        assertFalse(Lox.hasError());
        assertEquals("false", res);
    }

    //==== Concatenation ====
    @Test
    void stringConcatenation() {
        Object res = Lox.eval("\"hello\" + \"world\"");
        assertFalse(Lox.hasError());
        assertEquals("helloworld", res);
    }

    @Test
    void stringAndNumberConcatenation() {
        Object res = Lox.eval("\"hello\" + 133");
        assertFalse(Lox.hasError());
        assertEquals("hello133", res);
    }

    @Test
    void stringAndBooleanConcatenation() {
        Object res = Lox.eval("\"hello\" + true");
        assertFalse(Lox.hasError());
        assertEquals("hellotrue", res);
    }

    @Test
    void stringAndNilConcatenation() {
        Object res = Lox.eval("\"hello\" + nil");
        assertFalse(Lox.hasError());
        assertEquals("hellonil", res);
    }

    @Test
    void numberAndStringConcatenation() {
        Object res = Lox.eval("133 + \"str-123\"");
        assertFalse(Lox.hasError());
        assertEquals("133str-123", res);
    }

    @Test
    void booleanAndStringConcatenation() {
        Object res = Lox.eval("true + \"str-123\"");
        assertFalse(Lox.hasError());
        assertEquals("truestr-123", res);
    }

    //==== PLUS operator ====

    @Test
    void plusForIntegers() {
        Object res = Lox.eval("10+20");

        assertFalse(Lox.hasError());
        assertEquals("30", res);
    }

    @Test
    void plusForFloatingNumbers() {
        Object res = Lox.eval("10.23 + 20.45");

        assertFalse(Lox.hasError());
        assertEquals("30.68", res);
    }

    @Test
    void multiplePlusOperators() {
        Object res = Lox.eval("1+2+3+4+5");

        assertFalse(Lox.hasError());
        assertEquals("15", res);
    }

    @Test
    void plusForTwoBooleansShouldFail() {
        Lox.eval("true + false");
        assertTrue(Lox.hasError());
    }

    // ==== expressions ====
    @Test
    void singleNumericalExpression() {
        Object res = Lox.eval("(3*6) + 2");
        assertFalse(Lox.hasError());
        assertEquals("20", res);
    }

    @Test
    void singleEqEqLogicalExpression() {
        Object res = Lox.eval("(3*6) + 2 == 20");
        assertFalse(Lox.hasError());
        assertEquals("true", res);
    }

    @Test
    void singleBangEqLogicalExpression() {
        Object res = Lox.eval("(3*6) + 2 != 50");
        assertFalse(Lox.hasError());
        assertEquals("true", res);
    }

    @Test
    void commaSeparatedExpressions() {
        Object res = Lox.eval("(3*6) + 2 != 50, 2 * 4 == 8");
        assertFalse(Lox.hasError());
        assertEquals("true", res);
    }

    @Test
    void expressionsWithUnaryMinus() {
        Object res = Lox.eval("2 * (6 / -3)");

        assertFalse(Lox.hasError());
        assertEquals("-4", res);
    }

    @Test
    void complexExpressionWithGroupingAndUnaryMinus() {
        Object res = Lox.eval("((10+20) * 33.24) - 177.45 + (-20)");

        assertFalse(Lox.hasError());
        assertEquals("799.75", res);
    }

    @Test
    void groupingWithoutMatchingParenthesesShouldFail() {
        Lox.eval("(3*6 + 2 != 50");

        assertTrue(Lox.hasError());
        assertEquals("[line 1] Error: ')' expected", Lox.lastErrorMsg);
    }

    @Test
    void negateStringShouldFail() {
        Lox.eval("2 * (6 / -\"hello, world\")");

        assertTrue(Lox.hasError());
        assertEquals("[line 1]: Operand must be a number.", Lox.lastErrorMsg);
    }
}
