package org.max.crafting.interpreter.jlox.ast;

import org.max.crafting.interpreter.jlox.interpreter.StmtVisitor;
import org.max.crafting.interpreter.jlox.model.Token;

public class ReturnStmt implements Stmt {

    public final Token keyword;
    private final Expression expr;

    public ReturnStmt(Token keyword, Expression expr) {
        this.keyword = keyword;
        this.expr = expr;
    }

    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitReturnStmt(this);
    }
}
