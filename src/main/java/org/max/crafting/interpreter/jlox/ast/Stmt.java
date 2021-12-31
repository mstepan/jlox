package org.max.crafting.interpreter.jlox.ast;

import org.max.crafting.interpreter.jlox.interpreter.StmtVisitor;

public interface Stmt {

    <T> T accept(StmtVisitor<T> visitor);
}
