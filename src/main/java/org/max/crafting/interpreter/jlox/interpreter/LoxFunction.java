package org.max.crafting.interpreter.jlox.interpreter;

import org.max.crafting.interpreter.jlox.ast.Block;
import org.max.crafting.interpreter.jlox.ast.FunctionExpr;
import org.max.crafting.interpreter.jlox.ast.FunctionStmt;
import org.max.crafting.interpreter.jlox.model.Token;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * All user-defined functions will use this function object as a backbone.
 */
final class LoxFunction implements JLoxCallable {

    private final String name;
    private final List<Token> params;
    private final Block fnBody;
    private final Environment closure;

    /**
     * Create from function declaration.
     */
    public LoxFunction(FunctionStmt fnDeclaration, Environment closure) {
        Objects.requireNonNull(fnDeclaration, "'null' fnDeclaration detected");
        this.name = fnDeclaration.name.lexeme;
        this.params = fnDeclaration.parameters;
        this.fnBody = fnDeclaration.body;
        this.closure = Objects.requireNonNull(closure, "'null' closure environment detected");
    }

    /**
     * Create from function expression, a.k.a lambda function.
     */
    public LoxFunction(FunctionExpr fnExpr, Environment closure) {
        Objects.requireNonNull(fnExpr, "'null' fnExpr detected");
        this.name = "lambda-" + UUID.randomUUID();
        this.params = fnExpr.parameters;
        this.fnBody = fnExpr.body;
        this.closure = Objects.requireNonNull(closure, "'null' closure environment detected");
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {

        final Environment fnEnv = new Environment(closure);

        for (int i = 0; i < params.size(); ++i) {
            Token singleParam = params.get(i);
            Object singleArg = arguments.get(i);

            // bind arguments to parameters inside function scope
            fnEnv.defineInPlace(singleParam.lexeme, singleArg);
        }

        try {
            interpreter.executeStatements(fnBody.statements, fnEnv);
        }
        // handle RETURN statement flow
        catch (Return returnVal) {
            return returnVal.value;
        }


        return null;
    }

    @Override
    public int arity() {
        return params.size();
    }

    @Override
    public String toString() {
        return "<fun " + name + ">";
    }
}
