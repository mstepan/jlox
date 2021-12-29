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

        Lox.runScript(script);

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

    // ==== Scopes ====

    @Test
    void namesFromDifferentScopes() {
        Lox.runScript("""
                var x = 1;
                {
                    var y = 2;
                    print x;
                    print y;
                }                  
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
    void nameShadowingInNewScope() {
        Lox.runScript("""
                var x = 1;
                var y = 20;
                
                print x;
                print y;
                {
                    var x = x + 10;
                    var y = 30;
                    
                    print "inner x: " + x;                   
                    print "inner y: " + y;
                }   
                print x;   
                print y;             
                """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");

        assertEquals(
                """
                        1
                        20
                        inner x: 11
                        inner y: 30
                        1
                        20
                        """,
                output());

    }


    // ==== VAR declaration ====

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

    // ==== COMMA expression ====

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


    // ==== String concatenation ====

    @Test
    void stringConcatenation() {
        Lox.runScript("""
                              var res = "hello " + "world " + "!!!";
                              print res;
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
                              print res;
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
                              print res;
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
                              print res;
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
                              print res;
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
                              print res;
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        false hello
                         """,
                output());
    }

    //==== PLUS operator ====

    @Test
    void plusForIntegers() {
        Lox.runScript("""
                              var res = 10 + 20 + 30;
                              print res;
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        60
                         """,
                output());
    }

    @Test
    void plusForFloatingNumbers() {
        Lox.runScript("""
                              var res = 10.2 + 20.2 + 30.4;
                              print res;
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        60.8
                         """,
                output());
    }

    @Test
    void plusForIntegersAndFloats() {
        Lox.runScript("""
                              var res = 100 + 10.4 + 20.4 + 200 + 30.4;
                              print res;
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        361.2
                         """,
                output());
    }

    @Test
    void plusForTwoBooleansShouldFail() {
        Lox.runScript("""
                              var res = true + false;
                              print res;
                              """);

        assertTrue(Lox.hasError(), "Expected error here");
        assertEquals(
                """
                        [line 1]: Both operands must be numbers or one should be a string.
                         """,
                errorOutput());
    }

    @Test
    void plusForBooleanAndIntegerShouldFail() {
        Lox.runScript("""
                              var res = true + 133;
                              print res;
                              """);

        assertTrue(Lox.hasError(), "Expected error here");
        assertEquals(
                """
                        [line 1]: Both operands must be numbers or one should be a string.
                         """,
                errorOutput());
    }

    // ==== DOUBLE ====

    @Test
    void comparingNansShouldReturnFalse() {
        Lox.runScript("""
                              var res = 0.0/0.0 == 0.0/0.0;
                              print "res: " + res;
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        res: false
                         """,
                output());
    }

    // ==== DIVISION ====

    @Test
    void intDivisionByZeroShouldFail() {
        Lox.runScript("""
                              var x = 10/0;
                              """);

        assertTrue(Lox.hasError(), "Expected error here");

        assertEquals(
                """
                        [line 1]: Integer division by zero.
                         """,
                errorOutput());
    }

    // ==== Complex expression ====

    @Test
    void singleNumericalExpression() {
        Lox.runScript("""
                              var res = (3*6) + 2;
                              print res;
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        20
                         """,
                output());
    }

    // === LOGICAL expressions ====
    @Test
    void singleEqEqLogicalExpression() {
        Lox.runScript("""
                              var res = (3*6) + 2 == 20;
                              print res;
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        true
                         """,
                output());
    }

    @Test
    void singleNotEqLogicalExpression() {
        Lox.runScript("""
                              var res = (3*6) + 2 != 50;
                              print res;
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        true
                         """,
                output());
    }

    @Test
    void lessForInts() {
        Lox.runScript("""
                              var res = (3*3) + 2 < 20;
                              print res;
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        true
                         """,
                output());
    }

    @Test
    void greaterForInts() {
        Lox.runScript("""
                              var res = (3*3) + 2 > 20;
                              print res;
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        false
                         """,
                output());
    }

    // ==== UNARY minus ====
    @Test
    void unaryMinusForInt() {
        Lox.runScript("""
                              var res = 2 * (6 / -3);
                              print res;
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        -4
                         """,
                output());
    }

    @Test
    void unaryMinusForDoubleAndInt() {
        Lox.runScript("""
                              var res = -20.2 * (6 / -3);
                              print res;
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        40.4
                         """,
                output());
    }

    @Test
    void negateStringShouldFail() {
        Lox.runScript("""
                              var res = -"hello, world";
                              print res;
                              """);

        assertTrue(Lox.hasError(), "Expected error here");
        assertEquals(
                """
                        [line 1]: Operand must be a number.
                         """,
                errorOutput());
    }

    // ==== GROUPING ====

    @Test
    void complexExpressionWithGroupingAndUnaryMinus() {
        Lox.runScript("""
                              var res = ((10+20) * 33.24) - 177.45 + (-20);
                              print res;
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
                              print res;
                              """);

        assertTrue(Lox.hasError(), "Expected error here");
        assertEquals(
                """
                        [line 1] Matching ')' expected.
                         """,
                errorOutput());
    }

}
