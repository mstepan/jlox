package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test functionality related to print function.
 */
final class PrintFunctionTest extends LoxBaseTest {

    @Test
    void printBinaryExpressions() {
        Lox.runScript("""                                              
                              print(2 + 3 * 4);
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");

        assertEquals(
                """
                        14
                        """,
                output());

    }

    @Test
    void printStringConcatenation() {
        Lox.runScript("""                                              
                              print("hello" + " world" + " !!!");
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");

        assertEquals(
                """
                        hello world !!!
                        """,
                output());

    }

    @Test
    void printString() {
        Lox.runScript("""                                              
                              print("hello");
                              print("world");
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");

        assertEquals(
                """
                        hello
                        world
                        """,
                output());

    }
}
