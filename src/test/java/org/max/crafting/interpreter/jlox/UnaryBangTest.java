package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Test related to unary bang, also known as NOT, character '!'.
 */
final class UnaryBangTest extends LoxBaseTest {

    @Test
    void unaryBangForBoolean() {
        Lox.runScript("""
                              print(!true);
                              print(!false);
                              print(!!true);
                              print(!!false);
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        false
                        true
                        true
                        false
                         """,
                output());
    }
}
