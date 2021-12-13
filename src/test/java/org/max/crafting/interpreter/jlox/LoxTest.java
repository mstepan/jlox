package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.Test;
import org.max.crafting.interpreter.jlox.parser.ParseError;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class LoxTest {

    @Test
    void runCorrectExpression() {
        Lox.run("(3*6) + 2 != 50");
        assertFalse(Lox.hasError());
        assertEquals("(3.0 * 6.0) + 2.0 != 50.0", Lox.astRepr);
    }

    @Test
    void runGroupingWithoutMatchingParentheses() {
        assertThrows(ParseError.class, () -> {
            Lox.run("(3*6 + 2 != 50");
        });

        assertTrue(Lox.hasError());
        assertEquals("[line 1] Error: ')' expected", Lox.lastErrorMsg);
    }
}
