package org.max.crafting.interpreter.jlox.ast;

import org.max.crafting.interpreter.jlox.ast.visitor.NodeVisitor;
import org.max.crafting.interpreter.jlox.lexer.Token;

public class UnaryExpression implements Expression {

    private final Token operation;

    private final Expression expression;

    public UnaryExpression(Token operation, Expression expression) {
        this.operation = operation;
        this.expression = expression;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visitUnaryExpression(this);
    }

    public Token getOperation() {
        return operation;
    }

    public Expression getExpression() {
        return expression;
    }
}
