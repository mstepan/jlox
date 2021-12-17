package org.max.crafting.interpreter.jlox.ast;

import org.max.crafting.interpreter.jlox.ast.visitor.NodeVisitor;

public class Literal implements Expression {

    private final Object value;

    public Literal(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public Object accept(NodeVisitor visitor) {
        return visitor.visitLiteral(this);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
