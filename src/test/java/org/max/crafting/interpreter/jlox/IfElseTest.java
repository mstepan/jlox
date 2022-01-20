package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class IfElseTest extends LoxBaseTest {

    @Test
    void ifElseGoingIfBranch() {
        Lox.runScript("""
                              var x = 100;
                              var y = 20;
                                              
                              var z;
                                              
                              if( x > y ){
                                  z = x;
                              }
                              else {
                                  z = y;
                              }
                                              
                              print(z);

                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        100
                        """,
                output());
    }

    @Test
    void ifElseGoingElseBranch() {
        Lox.runScript("""
                              var x = 10;
                              var y = 20;
                                              
                              var z;
                                              
                              if( x > y ){
                                  z = x;
                              }
                              else {
                                  z = y;
                              }
                                              
                              print(z);

                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        20
                        """,
                output());
    }

    @Test
    void ifWithoutElse() {
        Lox.runScript("""
                              var x = 10;
                              var y = 20;
                                              
                              var maxValue = x;
                                              
                              if( y > maxValue ) maxValue = y;
                                        
                              print(maxValue);
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        20
                        """,
                output());
    }

    @Test
    void ifWithDanglingElse() {
        Lox.runScript("""
                              var x = 10;
                              var y = 20;
                              var z = 30;
                                              
                              var maxValue = 0;
                                              
                              if( x > maxValue ) {
                                maxValue = x;
                                if(y > z){
                                    maxValue = y;
                                }
                                else {
                                    maxValue = z;
                                }
                              }
                                        
                              print(maxValue);
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        30
                        """,
                output());
    }

    @Test
    void ifWithLogicalOr() {
        Lox.runScript("""
                              var x = 100;
                              var y = 0;
                                              
                              if( x == 0 or y == 0 ){
                                  print("YES");
                              }
                              else {
                                  print("NO");
                              }
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        YES
                        """,
                output());
    }

    @Test
    void ifWithLogicalOrCheckShortCircuit() {
        Lox.runScript("""
                              // logical OR should short-circuit here and print true
                              print(true or 2);
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        true
                        """,
                output());
    }

    @Test
    void ifWithLogicalAnd() {
        Lox.runScript("""
                              var x = 0;
                              var y = 0;
                                              
                              if( x == 0 and y == 0 ){
                                  print("YES");
                              }
                              else {
                                  print("NO");
                              }

                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        YES
                        """,
                output());
    }

    @Test
    void ifWithLogicalAndCheckShortCircuit() {
        Lox.runScript("""
                              // logical AND should short-circuit here and print false
                              print(false and 2);
                              
                              // nil is FALSE, so logical AND should short-circuit here
                              print(nil and 2);
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        false
                        nil
                        """,
                output());
    }

    @Test
    void elseWithoutIfShouldFail1() {
        Lox.runScript("""
                              else {
                                   var x = 20;
                              }
                              """);

        assertTrue(Lox.hasError(), "Expected error here");
        assertEquals(
                """
                        [line 1] Expected expression.
                        """,
                errorOutput());
    }

    @Test
    void elseWithoutIfShouldFail2() {
        Lox.runScript("""
                              var x = 10;
                              else {
                                   x = 20;
                              }
                              """);

        assertTrue(Lox.hasError(), "Expected error here");
        assertEquals(
                """
                        [line 1] Expected expression.
                        """,
                errorOutput());
    }
}
