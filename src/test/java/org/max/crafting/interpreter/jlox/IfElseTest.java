package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

final class IfElseTest extends LoxBaseTest {

    @Test
    void ifElseGoingIfBranch() {

        String script = """
                var x = 100;
                var y = 20;
                                
                var z;
                                
                if( x > y ){
                    z = x;
                }
                else {
                    z = y;
                }
                                
                print z;

                """;

        Lox.runScript(script);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        100
                        """,
                output());
    }

    @Test
    void ifElseGoingElseBranch() {

        String script = """
                var x = 10;
                var y = 20;
                                
                var z;
                                
                if( x > y ){
                    z = x;
                }
                else {
                    z = y;
                }
                                
                print z;

                """;

        Lox.runScript(script);

        assertFalse(Lox.hasError(), "Unexpected error(-s) detected");
        assertEquals(
                """
                        20
                        """,
                output());
    }
}
