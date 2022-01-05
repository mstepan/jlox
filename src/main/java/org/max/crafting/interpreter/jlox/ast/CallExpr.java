package org.max.crafting.interpreter.jlox.ast;

import org.max.crafting.interpreter.jlox.interpreter.ExpressionVisitor;
import org.max.crafting.interpreter.jlox.model.Token;

import java.util.List;

public class CallExpr implements Expression {

    public final Expression callee;

    public final List<Expression> arguments;

    // 'rightParent' will help generate meaningful error message during function call at runtime
    public final Token rightParen;

    public CallExpr(Expression callee, List<Expression> arguments, Token rightParen) {
        this.callee = callee;
        this.arguments = arguments;
        this.rightParen = rightParen;
    }

    @Override
    public Object accept(ExpressionVisitor visitor) {
        return visitor.visitCall(this);
    }
}
