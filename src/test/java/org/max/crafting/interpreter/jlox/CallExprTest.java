package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class CallExprTest extends LoxBaseTest {

    @Test
    void functionWithClosure() {
        Lox.runScript("""
                              fun createIncrement(){                              
                                
                                var cnt = 0;
                                
                                fun inc(){
                                    print cnt;
                                    cnt = cnt + 1;                                    
                                }
                                                            
                                return inc;                            
                              }
                              var incFunc = createIncrement();
                              incFunc();
                              incFunc();
                              incFunc();                              
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        0
                        1
                        2
                        """,
                output());
    }

    /**
     * First 5 fibonacci numbers, counting from zero: 1, 1, 2, 3, 5, 8
     */
    @Test
    void fibonacciRecursive() {
        Lox.runScript("""
                              fun fib(n){                              
                                if( n <= 1 ){
                                    return 1;
                                }
                                                            
                                return fib(n-1) + fib(n-2);                            
                              }
                              print fib(5);
                              """);
        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        8
                        """,
                output());
    }


    @Test
    void declareAndCallFunctionWithReturnStatement() {
        Lox.runScript("""
                              fun add(x, y){
                                return x + y;                            
                              }
                              print add(1, 2);
                              """);
        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        3
                        """,
                output());
    }

    @Test
    void declareAndCallCustomFunction() {
        Lox.runScript("""
                              fun sumOfTwo(x, y){
                                print "res: " + (x + y);                              
                              }
                              sumOfTwo(1, 2);
                              """);
        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        res: 3
                        """,
                output());
    }

    @Test
    void callRecursiveFunction() {
        Lox.runScript("""
                              fun countAndPrint(cur, boundary){
                                if( cur != boundary ){
                                    print cur;
                                    countAndPrint(cur+1, boundary);
                                }                             
                              }
                              countAndPrint(0, 5);
                              """);
        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        0
                        1
                        2
                        3
                        4
                        """,
                output());
    }


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
