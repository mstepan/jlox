package org.max.crafting.interpreter.jlox.ast;

import org.max.crafting.interpreter.jlox.ast.visitor.NodeVisitor;

public class Literal implements Expression {

    public final Object value;

    public Literal(Object value) {
        this.value = value;
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
