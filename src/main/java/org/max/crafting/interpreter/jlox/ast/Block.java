package org.max.crafting.interpreter.jlox.ast;

import org.max.crafting.interpreter.jlox.interpreter.StmtVisitor;

import java.util.ArrayList;
import java.util.List;

public class Block extends Stmt {

    public final List<Stmt> statements = new ArrayList<>();

    public void add(Stmt singleStmt) {
        statements.add(singleStmt);
    }

    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitBlockStatement(this);
    }
}
