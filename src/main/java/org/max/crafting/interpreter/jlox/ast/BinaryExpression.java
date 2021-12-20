package org.max.crafting.interpreter.jlox.ast;

import org.max.crafting.interpreter.jlox.ast.visitor.NodeVisitor;
import org.max.crafting.interpreter.jlox.model.Token;

public class BinaryExpression implements Expression {

    public final Expression left;

    public final Token operator;

    public final Expression right;

    public BinaryExpression(Expression left, Token operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public Object accept(NodeVisitor visitor) {
        return visitor.visitBinaryExpression(this);
    }
}
