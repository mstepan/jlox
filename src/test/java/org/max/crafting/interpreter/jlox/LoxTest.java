package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class LoxTest {

    @Test
    void runSingleExpression() {
        Lox.run("(3*6) + 2 != 50");
        assertFalse(Lox.hasError());
        assertEquals("(3.0 * 6.0) + 2.0 != 50.0", Lox.astRepr);
    }

    @Test
    void runCommaSeparatedExpressions() {
        Lox.run("(3*6) + 2 != 50, 2 * 2 == 8");
        assertFalse(Lox.hasError());
        assertEquals("(3.0 * 6.0) + 2.0 != 50.0 , 2.0 * 2.0 == 8.0", Lox.astRepr);
    }

    @Test
    void runGroupingWithoutMatchingParentheses() {
        Lox.run("(3*6 + 2 != 50");

        assertTrue(Lox.hasError());
        assertEquals("[line 1] Error: ')' expected", Lox.lastErrorMsg);
    }

}
