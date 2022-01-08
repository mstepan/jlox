package org.max.crafting.interpreter.jlox.interpreter;

import org.max.crafting.interpreter.jlox.model.Token;

import java.util.HashMap;
import java.util.Map;

public final class Environment {

    private final Map<String, Object> scope = new HashMap<>();

    private final Environment parent;

    public Environment() {
        this(null);
    }

    public Environment(Environment parent) {
        this.parent = parent;
    }

    /**
     * Declare new variable.
     */
    void define(String name, Object value) {
        // 1. we do not check if variable already declared, so we allow double declaration
        // 2. we also allow declared, but undefined variables with 'null' values
        scope.put(name, value);
    }

    Object get(Token name) {

        final String varName = name.lexeme;
        Map<String, Object> curScope = findScopeForVar(varName);

        if (curScope == null) {
            throw new RuntimeInterpreterException(name, "Undefined variable with name '" + name.lexeme + "'");
        }

        return curScope.get(varName);
    }

    /**
     * Reassign value to already existing variable.
     */
    void assign(Token name, Object newValue) {

        String varName = name.lexeme;
        Map<String, Object> curScope = findScopeForVar(varName);

        if (curScope == null) {
            // no implicit variable declaration, so fail here
            throw new RuntimeInterpreterException(name, "Can't assign to undefined variable '" + name.lexeme + "'.");
        }

        curScope.put(varName, newValue);
    }

    /**
     * Traverse all linked scopes and try to find variable declaration, otherwise just return null.
     */
    private Map<String, Object> findScopeForVar(String varName) {

        Environment cur = this;

        while (cur != null) {
            if (cur.scope.containsKey(varName)) {
                return cur.scope;
            }
            cur = cur.parent;
        }

        return null;
    }
}
