package org.max.crafting.interpreter.jlox.ast;

import org.max.crafting.interpreter.jlox.interpreter.ExpressionVisitor;
import org.max.crafting.interpreter.jlox.interpreter.StmtVisitor;
import org.max.crafting.interpreter.jlox.model.Token;

import java.util.List;

public class FunctionExpr implements Expression {

    public List<Token> parameters;
    public Block body;

    public FunctionExpr(List<Token> parameters, Block body) {
        this.parameters = parameters;
        this.body = body;
    }

    @Override
    public Object accept(ExpressionVisitor visitor) {
        return visitor.visitFunctionExpression(this);
    }
}
