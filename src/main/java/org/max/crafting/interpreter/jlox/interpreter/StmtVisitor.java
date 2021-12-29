package org.max.crafting.interpreter.jlox.interpreter;

import org.max.crafting.interpreter.jlox.ast.Block;
import org.max.crafting.interpreter.jlox.ast.ExpressionStmt;
import org.max.crafting.interpreter.jlox.ast.PrintStmt;
import org.max.crafting.interpreter.jlox.ast.VarStmt;

public interface StmtVisitor<T> {

    T visitExpressionStmt(ExpressionStmt stmt);

    T visitPrintStmt(PrintStmt stmt);

    T visitVarStmt(VarStmt stmt);

    T visitBlockStatement(Block blockStmt);

}
