package org.max.crafting.interpreter.jlox.interpreter;

import java.util.List;

public interface JLoxCallable {

    Object call(Interpreter interpreter, List<Object> params);

    int arity();
}
