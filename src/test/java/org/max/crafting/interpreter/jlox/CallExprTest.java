package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class CallExprTest extends LoxBaseTest {

    @Test
    void callMaxNativeFunction() {
        Lox.runScript("""
                              var x = 10;
                              var z = max(x, 133);
                              print z;
                              """);
        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        133
                        """,
                output());
    }

    @Test
    void callClockAndSleepFunctions() {
        Lox.runScript("""
                              var before = clock();
                              sleep(1);
                              var after = clock();
                              var timeElapsed = after - before;
                              
                              if( timeElapsed > 0.0 ){
                                print true;
                              }
                              else {
                                print false;
                              }
                              
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """ 
                        true
                        """,
                output());
    }

    @Test
    void callWithoutLastParenShouldFail() {
        Lox.runScript("""
                              max(10, 20;
                              """);

        assertTrue(Lox.hasError(), "Expected error here");
        assertEquals(
                """
                        [line 1] Expected ')' after arguments list.
                        """,
                errorOutput());
    }
}
