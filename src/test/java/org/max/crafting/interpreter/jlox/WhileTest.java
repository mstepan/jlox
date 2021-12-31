package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class WhileTest extends LoxBaseTest {

    @Test
    void whileSimpleLoop() {
        Lox.runScript("""
                              var x = 0;
                                              
                              while( x < 5 ){
                                print "loop: " + x;
                                x = x + 1;
                              }
                                                            
                              print "after loop: " + x;

                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        loop: 0
                        loop: 1
                        loop: 2
                        loop: 3
                        loop: 4
                        after loop: 5
                        """,
                output());
    }

    @Test
    void whilePrintTenFibonacciNumbers() {
        Lox.runScript("""
                              var first = 1;
                              var second = 1;                              
                                                            
                              var it = 0;
                                              
                              while( it < 10 ){
                                print first;
                                var temp = first + second;
                                first = second;
                                second = temp;
                                
                                it = it + 1;
                              }
                              print "all completed";

                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        1
                        1
                        2
                        3
                        5
                        8
                        13
                        21
                        34
                        55
                        all completed
                        """,
                output());
    }

    @Test
    void whileWithoutConditionShouldFail() {
        Lox.runScript("""
                              var x = 10;
                              while() {
                                x = x + 1;
                              }
                              """);

        assertTrue(Lox.hasError(), "Expected error here");
        assertEquals(
                """
                        [line 2] Expected expression.
                        """,
                errorOutput());
    }

    @Test
    void whileWithoutBodyShouldFail() {
        Lox.runScript("""
                              var x = 10;
                              while(x < 10);
                              var y = 20;
                              """);

        assertTrue(Lox.hasError(), "Expected error here");
        assertEquals(
                """
                        [line 2] Expected expression.
                        """,
                errorOutput());
    }

    @Test
    void whileWithoutLeftParentForConditionShouldFail() {
        Lox.runScript("""
                              var x = 0;
                              while x < 10){
                                x = x + 1;
                              }
                              """);

        assertTrue(Lox.hasError(), "Expected error here");
        assertEquals(
                """
                        [line 2] '(' expected after while.
                        """,
                errorOutput());
    }

    @Test
    void whileWithoutRightParentForConditionShouldFail() {
        Lox.runScript("""
                              var x = 0;
                              while(x < 10 {
                                x = x + 1;
                              }
                              """);

        assertTrue(Lox.hasError(), "Expected error here");
        assertEquals(
                """
                        [line 2] ')' expected after while condition.
                        [line 3] Expected expression.
                        """,
                errorOutput());
    }
}
