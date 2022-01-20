package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class ForTest extends LoxBaseTest {

    @Test
    void forSimpleLoop() {
        Lox.runScript("""
                              for(var x = 0; x < 5; x = x + 1){
                                print("x = " + x);
                              }

                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        x = 0
                        x = 1
                        x = 2
                        x = 3
                        x = 4
                        """,
                output());
    }

    @Test
    void forFibonacciSequence() {
        Lox.runScript("""
                              var a;
                              var b;
                              var it;
                              for(it = 0, a = 1, b = 1; it < 5; it = it + 1){
                                    print("it-" + it + ", value: " + a);
                                    var temp = a+b;
                                    a = b;
                                    b = temp;
                              }

                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        it-0, value: 1 
                        it-1, value: 1
                        it-2, value: 2
                        it-3, value: 3
                        it-4, value: 5
                        """,
                output());
    }

    @Test
    void forWithoutConditionShouldFail() {
        Lox.runScript("""
                              for(var x = 10;){
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
    void forWithoutIncrementShouldFail() {
        Lox.runScript("""
                              for(var x = 0; x < 10){
                              }
                              """);

        assertTrue(Lox.hasError(), "Expected error here");
        assertEquals(
                """
                        [line 1] Expected ';' after FOR loop condition.
                        """,
                errorOutput());
    }

    @Test
    void forWithoutLastParentShouldFail() {
        Lox.runScript("""
                              for(var x = 0; x < 10; x = x + 1{
                              }
                              """);

        assertTrue(Lox.hasError(), "Expected error here");
        assertEquals(
                """
                        [line 1] Expected ')' after FOR clauses.
                        """,
                errorOutput());
    }

}
