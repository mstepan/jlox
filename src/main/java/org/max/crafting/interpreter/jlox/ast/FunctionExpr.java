package org.max.crafting.interpreter.jlox.ast;

import org.max.crafting.interpreter.jlox.interpreter.ExpressionVisitor;
import org.max.crafting.interpreter.jlox.interpreter.StmtVisitor;
import org.max.crafting.interpreter.jlox.model.Token;

import java.util.List;
import java.util.Objects;

public class FunctionExpr implements Expression {

    public final List<Token> parameters;
    public final Block body;

    public FunctionExpr(List<Token> parameters, Block body) {
        this.parameters = parameters;
        this.body = Objects.requireNonNull(body, "null function body detected");
    }

    @Override
    public Object accept(ExpressionVisitor visitor) {
        return visitor.visitFunctionExpression(this);
    }
}
