package org.max.crafting.interpreter.jlox.interpreter.global;

import org.max.crafting.interpreter.jlox.interpreter.Interpreter;
import org.max.crafting.interpreter.jlox.interpreter.JLoxCallable;

import java.util.List;

/**
 * Returns max value from 2 integers.
 */
public final class MaxFunction implements JLoxCallable {

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        return Math.max((int) arguments.get(0), (int) arguments.get(1));
    }

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public String toString() {
        return "<native fun>";
    }

}
