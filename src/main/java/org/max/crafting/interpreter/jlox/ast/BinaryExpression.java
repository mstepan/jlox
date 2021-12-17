package org.max.crafting.interpreter.jlox.ast;

import org.max.crafting.interpreter.jlox.ast.visitor.NodeVisitor;
import org.max.crafting.interpreter.jlox.model.Token;

public class BinaryExpression implements Expression {

    private final Expression left;

    private final Token operation;

    private final Expression right;

    public BinaryExpression(Expression left, Token operation, Expression right) {
        this.left = left;
        this.operation = operation;
        this.right = right;
    }

    public Expression getLeft() {
        return left;
    }

    public Token getOperation() {
        return operation;
    }

    public Expression getRight() {
        return right;
    }

    @Override
    public Object accept(NodeVisitor visitor) {
        return visitor.visitBinaryExpression(this);
    }
}
