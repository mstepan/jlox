package org.max.crafting.interpreter.jlox.ast;

import org.max.crafting.interpreter.jlox.interpreter.StmtVisitor;

public class PrintStmt implements Stmt {

    public final Expression expr;

    public PrintStmt(Expression expr) {
        this.expr = expr;
    }

    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitPrintStmt(this);
    }
}
