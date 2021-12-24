package org.max.crafting.interpreter.jlox.ast;

import org.max.crafting.interpreter.jlox.interpreter.ExpressionVisitor;

public class Literal implements Expression {

    public final Object value;

    public Literal(Object value) {
        this.value = value;
    }

    @Override
    public Object accept(ExpressionVisitor visitor) {
        return visitor.visitLiteral(this);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
