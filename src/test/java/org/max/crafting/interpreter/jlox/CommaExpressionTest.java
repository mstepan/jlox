package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Tests related to comma expression functionality.
 */
final class CommaExpressionTest extends LoxBaseTest {

    @Test
    void commaExpression() {
        Lox.runScript("""            
                                              
                              print 10 + 20, 30 + 40;
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");

        assertEquals(
                """
                        70
                        """,
                output());

    }

    @Test
    void commaExpressionWithAssignments() {
        Lox.runScript("""
                              var x = 10;
                              var y = 20;
                              var z = 30;
                                              
                              var res = (y = z), (x = y), x + y + z;               
                                              
                              print res;
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");

        assertEquals(
                """
                        90
                        """,
                output());

    }

    @Test
    void commaExpressionWithMoreAssignments() {
        Lox.runScript(""" 
                              var a;
                              var b;
                              var c;
                              var d;               
                              a = 1, b = 2, c = 3, d = 4;               
                                              
                              print a;
                              print b;
                              print c;
                              print d;
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");

        assertEquals(
                """
                        1
                        2
                        3
                        4
                        """,
                output());

    }

    @Test
    void commaExpressionWithConstants() {
        Lox.runScript("""              
                              var res = (10, 20, 30);               
                                              
                              print res;
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");

        assertEquals(
                """
                        30
                        """,
                output());
    }
}
