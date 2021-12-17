package org.max.crafting.interpreter.jlox.ast;

import org.max.crafting.interpreter.jlox.ast.visitor.NodeVisitor;

public class Grouping implements Expression {

    private final Expression expression;

    public Grouping(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public Object accept(NodeVisitor visitor) {
        return visitor.visitParentheses(this);
    }
}
