package org.max.crafting.interpreter.jlox.ast;

import org.max.crafting.interpreter.jlox.interpreter.StmtVisitor;
import org.max.crafting.interpreter.jlox.model.Token;

public class VarStmt extends Stmt {

    public final Token name;
    public final Expression initExpr;

    public VarStmt(Token name, Expression initExpr) {
        this.name = name;
        this.initExpr = initExpr;
    }

    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitVarStmt(this);
    }
}
