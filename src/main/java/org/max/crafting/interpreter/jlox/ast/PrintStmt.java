package org.max.crafting.interpreter.jlox.ast;

import org.max.crafting.interpreter.jlox.ast.visitor.StmtVisitor;

public class PrintStmt extends Stmt {

    final Expression expr;

    public PrintStmt(Expression expr) {
        this.expr = expr;
    }

    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitPrintStmt(this);
    }
}
