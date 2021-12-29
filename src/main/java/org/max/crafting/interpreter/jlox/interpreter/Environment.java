package org.max.crafting.interpreter.jlox.interpreter;

import org.max.crafting.interpreter.jlox.model.Token;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public final class Environment {

    private final Deque<Map<String, Object>> scopes = new ArrayDeque<>();

    public Environment() {
        scopes.push(new HashMap<>());
    }

    Scope newScope() {
        scopes.push(new HashMap<>());
        return new Scope();
    }

    private void removeLastScope() {
        scopes.pop();
    }

    void clear() {
        last().clear();
    }

    /**
     * Declare new variable.
     */
    void define(String name, Object value) {
        // 1. we do not check if variable already declared, so we allow double declaration
        // 2. we also allow declared, but undefined variables with 'null' values
        last().put(name, value);
    }

    Object get(Token name) {

        final String varName = name.lexeme;
        Map<String, Object> curScope = findScopeForVar(varName);

        if (!curScope.containsKey(varName)) {
            throw new Interpreter.RuntimeError(name, "Undefined variable with name '" + name.lexeme + "'");
        }

        return curScope.get(varName);
    }

    /**
     * Reassign value to already existing variable.
     */
    void assign(Token name, Object newValue) {

        String varName = name.lexeme;
        Map<String, Object> curScope = findScopeForVar(varName);

        if (curScope.containsKey(varName)) {
            curScope.put(varName, newValue);
            return;
        }

        // no implicit variable declaration, so fail here
        throw new Interpreter.RuntimeError(name, "Can't assign to undefined variable '" + name.lexeme + "'.");
    }

    private Map<String, Object> last() {
        return scopes.peek();
    }

    private Map<String, Object> findScopeForVar(String varName) {
        for (Map<String, Object> singleScope : scopes) {
            if (singleScope.containsKey(varName)) {
                return singleScope;
            }
        }

        // return latest scope if can't find scope with variable
        return scopes.peek();
    }

    public class Scope implements AutoCloseable {
        @Override
        public void close() {
            removeLastScope();
        }
    }
}
