package org.max.crafting.interpreter.jlox.ast;

import org.max.crafting.interpreter.jlox.interpreter.ExpressionVisitor;
import org.max.crafting.interpreter.jlox.model.Token;

public class LogicalExpression implements Expression {

    public final Expression left;

    public final Token operator;

    public final Expression right;

    public LogicalExpression(Expression left, Token operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public Object accept(ExpressionVisitor visitor) {
        return visitor.visitLogicalExpression(this);
    }
}
