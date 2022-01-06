package org.max.crafting.interpreter.jlox.interpreter.global;

import org.max.crafting.interpreter.jlox.interpreter.Interpreter;
import org.max.crafting.interpreter.jlox.interpreter.JLoxCallable;

import java.util.List;

public final class MaxFunction implements JLoxCallable {

    @Override
    public Object call(Interpreter interpreter, List<Object> params) {
        return Math.max((int) params.get(0), (int) params.get(1));
    }

    @Override
    public int arity() {
        return 2;
    }

}
