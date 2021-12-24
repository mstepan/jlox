package org.max.crafting.interpreter.jlox.ast;

import org.max.crafting.interpreter.jlox.ast.visitor.StmtVisitor;

public class ExpressionStmt extends Stmt {

    final Expression expr;

    public ExpressionStmt(Expression expr) {
        this.expr = expr;
    }

    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitExpressionStmt(this);
    }
}
