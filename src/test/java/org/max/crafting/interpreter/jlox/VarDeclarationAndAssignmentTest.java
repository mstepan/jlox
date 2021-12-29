package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test functionality related to variable declaration and assignment.
 */
final class VarDeclarationAndAssignmentTest extends LoxBaseTest {

    // ==== Declaration ====

    @Test
    void varDeclaration() {
        Lox.runScript("""
                              var x = 1;
                              var y = 2;
                                              
                              print x;
                              print y;
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");

        assertEquals(
                """
                        1
                        2
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


    // ==== ASSIGNMENTS ====
    @Test
    void assignments() {
        Lox.runScript("""
                              var x = 1;
                              var y = 2;
                                          
                              var z;
                                              
                              z = x;
                              print "z = " + z;
                                              
                              z = (2*3) + 4;
                                              
                              print "z = " + z;

                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");

        assertEquals(
                """
                        z = 1
                        z = 10
                        """,
                output());

    }

    @Test
    void assignmentAsExpressionShouldReturnValue() {
        Lox.runScript("""
                              var x = 10;
                              print x = 20;
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");

        assertEquals(
                """ 
                        20
                         """,
                output());

    }

    @Test
    void assignDifferentTypeValuesToSameVariable() {
        Lox.runScript("""
                              var x = 10;
                              print x;
                                              
                              x = "hello";
                              print x;
                                              
                              x = 133.33;
                              print x;
                                              
                              x = true;
                              print x;
                                              
                              x = "string again";
                              print x;
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");

        assertEquals(
                """ 
                        10
                        hello
                        133.33
                        true
                        string again
                         """,
                output());

    }

    @Test
    void assignmentToStringShouldFail() {
        Lox.runScript("""
                              "hello" = 133;
                              """);

        assertTrue(Lox.hasError(), "Error wasn't detected, strange");
        assertEquals(
                """
                        [line 1] Invalid assignment target.
                        """,
                errorOutput());
    }

    @Test
    void assignmentToNumberShouldFail() {
        Lox.runScript("""
                              var x = 133;
                              155 = x;
                              """);

        assertTrue(Lox.hasError(), "Error wasn't detected, strange");
        assertEquals(
                """
                        [line 2] Invalid assignment target.
                        """,
                errorOutput());
    }

    @Test
    void assignmentWithoutDeclarationShouldFail() {
        Lox.runScript("""
                              var y = 10 + 20;
                              x = 133;
                              print x;
                              """);

        assertTrue(Lox.hasError(), "Expected error here");

        assertEquals(
                """
                        [line 2]: Can't assign to undefined variable 'x'.
                         """,
                errorOutput());
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
}
