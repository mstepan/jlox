package org.max.crafting.interpreter.jlox.interpreter;

import org.max.crafting.interpreter.jlox.ast.Block;
import org.max.crafting.interpreter.jlox.ast.ExpressionStmt;
import org.max.crafting.interpreter.jlox.ast.IfStmt;
import org.max.crafting.interpreter.jlox.ast.PrintStmt;
import org.max.crafting.interpreter.jlox.ast.VarStmt;
import org.max.crafting.interpreter.jlox.ast.WhileStmt;

public interface StmtVisitor<T> {

    T visitExpressionStmt(ExpressionStmt stmt);

    T visitPrintStmt(PrintStmt stmt);

    T visitVarStmt(VarStmt stmt);

    T visitBlockStatement(Block blockStmt);

    T visitIfStatement(IfStmt ifStmt);

    T visitWhileStatement(WhileStmt whileStmt);
}
