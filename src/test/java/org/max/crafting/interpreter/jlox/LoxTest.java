package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class LoxTest {

    private static final ByteArrayOutputStream OUT = new ByteArrayOutputStream();
    private static final ByteArrayOutputStream ERR = new ByteArrayOutputStream();

    private static String output() {
        return new String(OUT.toByteArray(), Charset.defaultCharset());
    }

    private static String errorOutput() {
        return new String(ERR.toByteArray(), Charset.defaultCharset());
    }

    @BeforeAll
    static void setUp() {
        System.setOut(new PrintStream(OUT));
        System.setErr(new PrintStream(ERR));
    }

    @AfterEach
    void tearDown() {
        OUT.reset();
        ERR.reset();
    }

    @Test
    void runNormalScript() {

        String script = """
                // some dummy unused variables declaration
                var x = 10;
                var y = 20;
                var z;
                            
                print "x = " + x;
                print "y = " + y;
                print "Uninitialized variable is set to: " + z;
                            
                // assignment var to var
                x = y;
                            
                print "x = " + x;
                print "y = " + y;
                            
                // assigment var to expression
                x = (2*3) + 4;
                            
                print "x = " + x;
                            
                // radius for a circle as int
                var r = 10;
                            
                // PI constant as a double
                var pi = 3.14;
                            
                // printing some message
                print "calculating area for circle with radius: " + r + " m";
                            
                var area = pi * r * r;
                            
                print "area: " + area + " m^2";

                """;

        Lox.run(script);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        x = 10
                        y = 20
                        Uninitialized variable is set to: nil
                        x = 20
                        y = 20
                        x = 10
                        calculating area for circle with radius: 10 m
                        area: 314.0 m^2
                        """,
                output());
    }

    @Test
    void test123() {

        String script = """
                x = 133;
                print x;
                """;

        Lox.run(script);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");

        assertEquals(
                """
                        133
                        """,
                output());

    }

    // ==== VAR declaration ====

    @Test
    void varDeclaration() {

        String script = """
                var x = 1;
                var y = 2;
                
                print x;
                print y;
                """;

        Lox.run(script);

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

        String script = """
                var x = 1;
                var y = 2;
                var x = 133;
                
                print x;
                print y;
                
                var x = 155;
                print x;
                """;

        Lox.run(script);

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

        String script = """
                var x = 1;
                var y = 2;
                            
                var z;
                                
                z = x;
                print "z = " + z;
                                
                z = (2*3) + 4;
                                
                print "z = " + z;

                """;

        Lox.run(script);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");

        assertEquals(
                """
                        z = 1
                        z = 10
                        """,
                output());

    }

    @Test
    void assignmentToStringShouldFail() {
        String script = """
                "hello" = 133;
                """;

        Lox.run(script);

        assertTrue(Lox.hasError(), "Error wasn't detected, strange");
        assertEquals(
                """
                        [line 1] Error: Invalid assignment target.
                        """,
                errorOutput());
    }

    @Test
    void assignmentToNumberShouldFail() {
        String script = """
                var x = 133;
                155 = x;
                """;

        Lox.run(script);

        assertTrue(Lox.hasError(), "Error wasn't detected, strange");
        assertEquals(
                """
                        [line 2] Error: Invalid assignment target.
                        """,
                errorOutput());
    }
}
