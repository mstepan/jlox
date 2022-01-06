package org.max.crafting.interpreter.jlox.interpreter.global;

import org.max.crafting.interpreter.jlox.interpreter.Interpreter;
import org.max.crafting.interpreter.jlox.interpreter.JLoxCallable;

import java.util.List;

public final class ClockFunction implements JLoxCallable {

    @Override
    public Object call(Interpreter interpreter, List<Object> params) {
        return System.currentTimeMillis();
    }

    @Override
    public int arity() {
        return 0;
    }
}
