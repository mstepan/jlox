package org.max.crafting.interpreter.jlox.ast;

import org.max.crafting.interpreter.jlox.interpreter.ExpressionVisitor;

public class CommaExpr implements Expression {

    public final  Expression left;
    public final  Expression right;

    public CommaExpr(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Object accept(ExpressionVisitor visitor) {
        return visitor.visitCommaExpression(this);
    }
}
