package org.max.crafting.interpreter.jlox.interpreter;

import org.max.crafting.interpreter.jlox.model.Token;

/**
 * Runtime exception during AST evaluation.
 */
public final class RuntimeInterpreterException extends RuntimeException {

    public final Token operator;

    RuntimeInterpreterException(Token operator, String errorMsg) {
        super(errorMsg);
        this.operator = operator;
    }

}
