package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

final class LoxTest extends LoxBaseTest {

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

    // ==== Numerical only expression ====

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
}
