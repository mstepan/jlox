package org.max.crafting.interpreter.jlox.ast;


import org.max.crafting.interpreter.jlox.interpreter.StmtVisitor;

public class IfStmt extends Stmt {

    public final Expression condition;
    public final Stmt thenBranch;
    public final Stmt elseBranch;

    public IfStmt(Expression condition, Stmt thenBranch, Stmt elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitIfStatement(this);
    }
}
