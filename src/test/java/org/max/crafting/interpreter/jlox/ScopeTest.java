package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test all functionality related to lexical scopes.
 */
final class ScopeTest extends LoxBaseTest {


    @Test
    void namesFromDifferentScopes() {
        Lox.runScript("""
                              var x = 1;
                              {
                                  var y = 2;
                                  print(x);
                                  print(y);
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
                                              
                              print(x);
                              print(y);
                              {
                                  var x = x + 10;
                                  var y = 30;
                                  
                                  print("inner x: " + x);                   
                                  print("inner y: " + y);
                              }   
                              print(x);   
                              print(y);             
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

    @Test
    void deepScopeNesting() {
        Lox.runScript("""
                              var x = 10;
                              var y = 20;
                              var z = 30;
                                              
                              print("outer x: " + x);
                              print("outer y: " +y);
                              print("outer z: " + z);
                              {
                                  var x = 11;
                                  
                                  {
                                      var y  = 22;
                                      
                                      {
                                          var z = 33;
                                          print("deep inner x: " + x);
                                          print("deep inner y: " + y);
                                          print("deep inner z: " + z);
                                      }
                                      
                                   }
                                  
                                  print("inner x: " + x);
                                  print("inner y: " + y);
                                  print("inner z: " + z);
                              }   
                              print("last x: " + x);   
                              print("last y: " + y);
                              print("last z: " + z);           
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");

        assertEquals(
                """
                        outer x: 10
                        outer y: 20
                        outer z: 30
                        deep inner x: 11
                        deep inner y: 22
                        deep inner z: 33 
                        inner x: 11
                        inner y: 20
                        inner z: 30
                        last x: 10
                        last y: 20
                        last z: 30
                        """,
                output());

    }

    @Test
    void assignToGlobalVariableFromNestedScope() {
        Lox.runScript("""
                              var x = 1;
                              print("outer x: " + x);
                              {
                                  var y = 2;
                                  x = 10;
                                  print("inner x: " + x);
                                  print("inner y: " + y);
                              } 
                              print("last x: " + x);               
                              """);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");

        assertEquals(
                """
                        outer x: 1
                        inner x: 10
                        inner y: 2
                        last x: 10
                        """,
                output());

    }

    @Test
    void blockWithoutBraceShouldFail() {
        Lox.runScript("""
                              var x = 1;
                              {
                                   var y = 2;           
                               """);

        assertTrue(Lox.hasError(), "Expected error here");

        assertEquals(
                """
                        [line 3] '}' expected at the end of block.
                        """,
                errorOutput());

    }

    @Test
    void synchronizationPointWorkingWithinNestedScope() {
        Lox.runScript("""
                              var x = 1;
                              print(x);
                              {
                                  var y = 2
                              }
                              var z = 133;           
                              """);

        assertTrue(Lox.hasError(), "Expected error here");

        assertEquals(
                """
                        [line 4] Expected ';' after variable declaration.    
                        [line 6] '}' expected at the end of block.                   
                        """,
                errorOutput());
    }

}
