package org.max.crafting.interpreter.jlox.ast;

import org.max.crafting.interpreter.jlox.interpreter.ExpressionVisitor;
import org.max.crafting.interpreter.jlox.model.Token;

public class UnaryExpression implements Expression {

    public final Token operation;

    public final Expression expression;

    public UnaryExpression(Token operation, Expression expression) {
        this.operation = operation;
        this.expression = expression;
    }

    @Override
    public Object accept(ExpressionVisitor visitor) {
        return visitor.visitUnaryExpression(this);
    }
}
