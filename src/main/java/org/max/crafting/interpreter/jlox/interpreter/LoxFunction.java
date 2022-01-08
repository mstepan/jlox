package org.max.crafting.interpreter.jlox.interpreter;

import org.max.crafting.interpreter.jlox.ast.FunctionStmt;
import org.max.crafting.interpreter.jlox.model.Token;

import java.util.List;
import java.util.Objects;

public class LoxFunction implements JLoxCallable {

    private final FunctionStmt fnDeclaration;

    public LoxFunction(FunctionStmt fnDeclaration) {
        this.fnDeclaration = Objects.requireNonNull(fnDeclaration, "'null' fnDeclaration detected");
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {

        try (Environment.Scope fnScope = interpreter.globals.newScope()) {
            for (int i = 0; i < fnDeclaration.parameters.size(); ++i) {
                Token singleParam = fnDeclaration.parameters.get(i);
                Object singleArg = arguments.get(i);

                // bind arguments to parameters inside function scope
                fnScope.define(singleParam.lexeme, singleArg);
            }

            interpreter.executeBlock(fnDeclaration.body);
        }

        return null;
    }

    @Override
    public int arity() {
        return fnDeclaration.parameters.size();
    }
}
