package org.max.crafting.interpreter.jlox.interpreter;

import org.max.crafting.interpreter.jlox.model.Token;

import java.util.HashMap;
import java.util.Map;

public final class Environment {

    private final Map<String, Object> values = new HashMap<>();

    void define(String name, Object value) {
        values.put(name, value);
    }

    Object get(Token name) {

        Object value = values.get(name.lexeme);

        if (value == null) {
            throw new Interpreter.RuntimeError(name, "Undefined variable with name '" + name.lexeme + "'");
        }

        return value;
    }
}
