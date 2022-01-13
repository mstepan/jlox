package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test functionality related to variable declaration.
 */
final class VarDeclarationTest extends LoxBaseTest {

    @Test
    void declareVariableForFunction() {
        Lox.runScript("""
                              fun add(x, y){
                                return x+y;
                              }                              
                              var add2 = add;
                              
                              print add2(1, 2);

                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");

        assertEquals(
                """
                        3
                        """,
                output());

    }

    @Test
    void varDeclaration() {
        Lox.runScript("""
                              var x = 1; // with init expression
                              var y; // without init
                                              
                              print x;
                              print y;
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");

        assertEquals(
                """
                        1
                        nil
                        """,
                output());

    }

    @Test
    void redeclareVariableFewTimesShouldBeOK() {
        Lox.runScript("""
                              var x = 1;
                              var y = 2;
                              var x = 133;
                                              
                              print x;
                              print y;
                                              
                              var x = 155;
                              print x;
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");

        assertEquals(
                """
                        133
                        2
                        155
                        """,
                output());

    }

    @Test
    void varWithoutCommaShouldFail() {
        Lox.runScript("""
                              var x = 133;
                              var y = 155
                              var z = 777;
                              """);

        assertTrue(Lox.hasError(), "Error wasn't detected, strange");
        assertEquals(
                """
                        [line 2] Expected ';' after variable declaration.
                        """,
                errorOutput());
    }

    @Test
    void varWithoutIdentifierShouldFail() {
        Lox.runScript("""
                              var = 133;
                              """);

        assertTrue(Lox.hasError(), "Error wasn't detected, strange");
        assertEquals(
                """
                        [line 1] Variable name expected.
                        """,
                errorOutput());
    }
}
