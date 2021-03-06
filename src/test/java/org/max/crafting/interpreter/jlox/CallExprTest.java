package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class CallExprTest extends LoxBaseTest {

    @Test
    void callNestedLambdaFunctionsWithClosuresImmediately() {
        Lox.runScript("""                        
                              fun(msg1){ 
                                return fun(msg2) {
                                    print("prefix: " + msg1 + msg2);
                                };
                              }("hello")("123");                                                    
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        prefix: hello123
                        """,
                output());
    }


    @Test
    void lambdaExpression() {
        Lox.runScript("""
                              fun(){};
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals("", output());
    }

    @Test
    void callLambdaFunctionImmediately() {
        Lox.runScript("""                        
                              fun(msg){ 
                                print(msg); 
                              }("hello-lambda-111");                                                    
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        hello-lambda-111
                        """,
                output());
    }

    @Test
    void callLambdaFunctionWithBindingToVariable() {
        Lox.runScript("""                        
                              var someFun = fun(msg){ 
                                print(msg); 
                              };   
                              someFun("hello-123");                                                    
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        hello-123
                        """,
                output());
    }

    @Test
    void callLambdaFunctionAsArgumentForAnotherFunction() {
        Lox.runScript("""                        
                              fun print2Times( fnToCall ){
                                 for(var i = 0; i < 2; i = i + 1){
                                     fnToCall();
                                 }
                               }

                               print2Times(fun(){
                                 print("hello");
                                 });                                                  
                               """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        hello
                        hello
                        """,
                output());
    }

    @Test
    void returnLambdaFromFunction() {
        Lox.runScript("""              
                              fun factory(){
                                return fun(msg){
                                    print(msg);
                                };
                              }          
                              var someFn1 = factory(); 
                              someFn1("hello");                                                    
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        hello
                        """,
                output());
    }

    @Test
    void notLeakingClosureScopeWithAssignment() {
        Lox.runScript("""
                              var str = "global"; 
                                                            
                              fun createClosure(){
                                var str = "local-1";
                                
                                fun printMe(){
                                    print(str);
                                }
                                
                                str = "local-2";
                                
                                return printMe;
                              }
                                                            
                              var someFn = createClosure();
                                                            
                              var str = "global";                               
                              someFn();  
                                                            
                              var str = "global";                                                           
                              someFn();
                                                       
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        local-2
                        local-2
                        """,
                output());
    }

    @Test
    void notLeakingClosureScopeWithVarDeclaration() {
        Lox.runScript("""
                              var str = "global"; 
                              {                            
                                fun printMe(){
                                    print(str);                                 
                                }
                                
                                printMe();
                                var str = "local";
                                printMe();                                 
                              }                             
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        global
                        global
                        """,
                output());
    }


    @Test
    void functionWithClosure() {
        Lox.runScript("""
                              fun createIncrement(){                              
                                
                                var cnt = 0;
                                
                                fun inc(){
                                    print(cnt);
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

    @Test
    void callRecursiveFunctionWithoutReturn() {
        Lox.runScript("""
                              fun countAndPrint(cur, boundary){
                                if( cur != boundary ){
                                    print(cur);
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
                              var res = fib(5);
                              print(res);
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
                              print(add(1, 2));
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
                                print("res: " + (x + y));                              
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
    void callMaxNativeFunction() {
        Lox.runScript("""
                              var x = 10;
                              var z = max(x, 133);
                              print(z);
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
                                print(true);
                              }
                              else {
                                print(false);
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

    @Test
    void moreArgumentThanParametersShouldFail() {
        Lox.runScript("""
                              fun sum(x, y, z){
                                return x + y + z;
                              }
                                  
                              sum(1, 2, 3, 4, 5);                        
                              """);

        assertTrue(Lox.hasError(), "Expected error here");
        assertEquals(
                """
                        [line 5]: Expected 3 parameters for <fun sum>, but passed 5.
                        """,
                errorOutput());
    }

    @Test
    void lessArgumentsThanParametersShouldFail() {
        Lox.runScript("""
                              fun sum(x, y, z){
                                return x + y + z;
                              }
                                  
                              sum(1, 2);                        
                              """);

        assertTrue(Lox.hasError(), "Expected error here");
        assertEquals(
                """
                        [line 5]: Expected 3 parameters for <fun sum>, but passed 2.
                        """,
                errorOutput());
    }
}
