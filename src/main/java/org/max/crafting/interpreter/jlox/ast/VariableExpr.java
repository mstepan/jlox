package org.max.crafting.interpreter.jlox.ast;

import org.max.crafting.interpreter.jlox.interpreter.ExpressionVisitor;
import org.max.crafting.interpreter.jlox.model.Token;

public class VariableExpr implements Expression {

    public final Token name;

    public VariableExpr(Token name) {
        this.name = name;
    }

    @Override
    public Object accept(ExpressionVisitor visitor) {
        return visitor.visitVariableExpression(this);
    }

    @Override
    public String toString(){
        return name.lexeme;
    }
}
