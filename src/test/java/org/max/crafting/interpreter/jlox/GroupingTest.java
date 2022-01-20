package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test related to grouping functionality.
 */
final class GroupingTest extends LoxBaseTest {

    @Test
    void complexExpressionWithGroupingAndUnaryMinus() {
        Lox.runScript("""
                              var res = ((10+20) * 33.24) - 177.45 + (-20);
                              print(res);
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
                              print(res);
                              """);

        assertTrue(Lox.hasError(), "Expected error here");
        assertEquals(
                """
                        [line 1] Matching ')' expected.
                         """,
                errorOutput());
    }

}
