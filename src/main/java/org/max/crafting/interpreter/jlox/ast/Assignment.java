package org.max.crafting.interpreter.jlox.ast;

import org.max.crafting.interpreter.jlox.interpreter.ExpressionVisitor;
import org.max.crafting.interpreter.jlox.model.Token;

public class Assignment implements Expression {

    public final Token name;

    public final Expression value;

    public Assignment(Token name, Expression value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public Object accept(ExpressionVisitor visitor) {
        return visitor.visitAssignmentExpression(this);
    }
}
