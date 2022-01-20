package org.max.crafting.interpreter.jlox.interpreter.global;

import org.max.crafting.interpreter.jlox.interpreter.Interpreter;
import org.max.crafting.interpreter.jlox.interpreter.JLoxCallable;

import java.util.List;

public class PrintFunction implements JLoxCallable {

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {

        System.out.println(Interpreter.stringify(arguments.get(0)));

        return null;
    }

    @Override
    public int arity() {
        return 1;
    }
}
