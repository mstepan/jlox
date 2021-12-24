package org.max.crafting.interpreter.jlox.ast;

import org.max.crafting.interpreter.jlox.interpreter.ExpressionVisitor;

public class Grouping implements Expression {

    public final Expression expression;

    public Grouping(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Object accept(ExpressionVisitor visitor) {
        return visitor.visitParentheses(this);
    }
}
