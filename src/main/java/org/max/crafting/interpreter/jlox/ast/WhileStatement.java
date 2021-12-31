package org.max.crafting.interpreter.jlox.ast;

import org.max.crafting.interpreter.jlox.interpreter.StmtVisitor;

public class WhileStatement extends Stmt {

    public final Expression condition;
    public final Stmt body;

    public WhileStatement(Expression condition, Stmt body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitWhileStatement(this);
    }
}
