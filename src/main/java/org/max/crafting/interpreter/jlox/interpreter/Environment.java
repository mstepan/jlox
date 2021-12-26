package org.max.crafting.interpreter.jlox.interpreter;

import org.max.crafting.interpreter.jlox.model.Token;

import java.util.HashMap;
import java.util.Map;

public final class Environment {

    private final Map<String, Object> values = new HashMap<>();

    void clear(){
        values.clear();
    }

    void define(String name, Object value) {
        // 1. we do not check if variable already declared, so we allow double declaration
        // 2. we also allow declared, but undefined variables with 'null' values
        values.put(name, value);
    }

    boolean isDefined(Token name){
        return values.containsKey(name.lexeme);
    }


    Object get(Token name) {

        final String varName = name.lexeme;

        if( ! values.containsKey(varName) ){
            throw new Interpreter.RuntimeError(name, "Undefined variable with name '" + name.lexeme + "'");
        }

        return  values.get(varName);
    }
}
