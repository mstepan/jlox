package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class LoxTest {

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
    void multipleOperators() {
        Lox.run("1+2+3+4+5");

        assertFalse(Lox.hasError());
        assertEquals("15", Lox.result);
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
