package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class LoxTest {

    @Test
    void runCorrectExpression(){
        Lox.run("(3*6) + 2 != 50");
        assertEquals("(3.0 * 6.0) + 2.0 != 50.0", Lox.astRepr);
    }

}
