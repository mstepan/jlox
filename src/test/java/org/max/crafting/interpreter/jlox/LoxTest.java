package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class LoxTest {

    @Test
    void runSingleNumericalExpression() {
        Lox.run("(3*6) + 2");
        assertFalse(Lox.hasError());
        assertEquals(20.0, Lox.result);
    }

    @Test
    void runSingleEqEqLogicalExpression() {
        Lox.run("(3*6) + 2 == 20");
        assertFalse(Lox.hasError());
        assertEquals(true, Lox.result);
    }

    @Test
    void runSingleBangEqLogicalExpression() {
        Lox.run("(3*6) + 2 != 50");
        assertFalse(Lox.hasError());
        assertEquals(true, Lox.result);
    }

    @Test
    void runCommaSeparatedExpressions() {
        Lox.run("(3*6) + 2 != 50, 2 * 4 == 8");
        assertFalse(Lox.hasError());
        assertEquals(true, Lox.result);
    }

    /**
     * This test case works just check that we use Double.equals(...) for '==' comparison,
     * which in java returns 'true' for NaN.equals(NaN).
     */
    @Test
    void comparingNans() {
        Lox.run("0/0 == 0/0");

        assertFalse(Lox.hasError());
        assertEquals(true, Lox.result);
    }

    @Test
    void runGroupingWithoutMatchingParentheses() {
        Lox.run("(3*6 + 2 != 50");

        assertTrue(Lox.hasError());
        assertEquals("[line 1] Error: ')' expected", Lox.lastErrorMsg);
    }
}
