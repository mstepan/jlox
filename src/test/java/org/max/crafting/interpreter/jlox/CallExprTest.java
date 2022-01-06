package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

final class CallExprTest extends LoxBaseTest {

    @Test
    void simpleCall() {
        Lox.runScript("""
                              var x = 10;
                              var z = max(x, 133);
                              print z;
                              """);

//        assertEquals("", errorOutput());
        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        133
                        """,
                output());
    }


//    @Test
//    void testFailed1() {
//        Lox.runScript("""
//                              var x = 0;
//                              while(x < 10 {
//                                x = x + 1;
//                              }
//                              """);
//
//        assertTrue(Lox.hasError(), "Expected error here");
//        assertEquals(
//                """
//                        [line 2] ')' expected after while condition.
//                        [line 3] Expected expression.
//                        """,
//                errorOutput());
//    }
}
