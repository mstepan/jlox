package org.max.crafting.interpreter.jlox.interpreter;

import org.max.crafting.interpreter.jlox.ast.FunctionStmt;
import org.max.crafting.interpreter.jlox.model.Token;

import java.util.List;
import java.util.Objects;

/**
 * All user-defined functions will use this function object as a backbone.
 */
final class LoxFunction implements JLoxCallable {

    private final FunctionStmt fnDeclaration;
    private final Environment closure;

    public LoxFunction(FunctionStmt fnDeclaration, Environment closure) {
        this.fnDeclaration = Objects.requireNonNull(fnDeclaration, "'null' fnDeclaration detected");
        this.closure = Objects.requireNonNull(closure, "'null' closure environment detected");
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {

        Environment fnEnv = new Environment(closure);

        for (int i = 0; i < fnDeclaration.parameters.size(); ++i) {
            Token singleParam = fnDeclaration.parameters.get(i);
            Object singleArg = arguments.get(i);

            // bind arguments to parameters inside function scope
            fnEnv.define(singleParam.lexeme, singleArg);
        }

        // handle RETURN statement flow
        try {
            interpreter.executeStatements(fnDeclaration.body.statements, fnEnv);
        }
        catch (Return returnVal) {
            return returnVal.value;
        }


        return null;
    }

    @Override
    public int arity() {
        return fnDeclaration.parameters.size();
    }

    @Override
    public String toString() {
        return "<fun " + fnDeclaration.name.lexeme + ">";
    }
}
