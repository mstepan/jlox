package org.max.crafting.interpreter.jlox.interpreter;

import org.max.crafting.interpreter.jlox.ast.ExpressionStmt;
import org.max.crafting.interpreter.jlox.ast.PrintStmt;

public interface StmtVisitor<T> {

    T visitExpressionStmt(ExpressionStmt stmt);

    T visitPrintStmt(PrintStmt stmt);

}
