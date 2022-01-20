package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Test related to String concatenation functionality.
 */
final class StringConcatenationTest extends LoxBaseTest {

    @Test
    void stringConcatenation() {
        Lox.runScript("""
                              var res = "hello " + "world " + "!!!";
                              print(res);
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        hello world !!!
                         """,
                output());
    }

    @Test
    void stringAndNumberConcatenation() {
        Lox.runScript("""
                              var res = "hello " + 133;
                              print(res);
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        hello 133
                         """,
                output());
    }

    @Test
    void stringAndBooleanConcatenation() {
        Lox.runScript("""
                              var res = "hello " + true;
                              print(res);
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        hello true
                         """,
                output());
    }

    @Test
    void stringAndNilConcatenation() {
        Lox.runScript("""
                              var res = "hello " + nil;
                              print(res);
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        hello nil
                         """,
                output());
    }

    @Test
    void numberAndStringConcatenation() {
        Lox.runScript("""
                              var res = 133 + " hello";
                              print(res);
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        133 hello
                         """,
                output());
    }

    @Test
    void booleanAndStringConcatenation() {
        Lox.runScript("""
                              var res = false + " hello";
                              print(res);
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        false hello
                         """,
                output());
    }
}
