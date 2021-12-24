package org.max.crafting.interpreter.jlox.ast;

import org.max.crafting.interpreter.jlox.ast.visitor.StmtVisitor;

public abstract class Stmt {

    public abstract <T> T accept(StmtVisitor<T> visitor);
}