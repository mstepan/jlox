package org.max.crafting.interpreter.jlox.interpreter;

import org.max.crafting.interpreter.jlox.ast.Assignment;
import org.max.crafting.interpreter.jlox.ast.BinaryExpr;
import org.max.crafting.interpreter.jlox.ast.CommaExpr;
import org.max.crafting.interpreter.jlox.ast.Literal;
import org.max.crafting.interpreter.jlox.ast.Grouping;
import org.max.crafting.interpreter.jlox.ast.LogicalExpr;
import org.max.crafting.interpreter.jlox.ast.UnaryExpr;
import org.max.crafting.interpreter.jlox.ast.VariableExpr;

public interface ExpressionVisitor {

    Object visitBinaryExpression(BinaryExpr binaryExp);

    Object visitUnaryExpression(UnaryExpr unaryExp);

    Object visitLiteral(Literal literal);

    Object visitParentheses(Grouping grouping);

    Object visitVariableExpression(VariableExpr varExpr);

    Object visitAssignmentExpression(Assignment assignment);

    Object visitCommaExpression(CommaExpr commaExpr);

    Object visitLogicalExpression(LogicalExpr logicalExpr);
}
