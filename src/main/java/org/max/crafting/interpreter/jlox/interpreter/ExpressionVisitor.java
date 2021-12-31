package org.max.crafting.interpreter.jlox.interpreter;

import org.max.crafting.interpreter.jlox.ast.Assignment;
import org.max.crafting.interpreter.jlox.ast.BinaryExpression;
import org.max.crafting.interpreter.jlox.ast.CommaExpresssion;
import org.max.crafting.interpreter.jlox.ast.Literal;
import org.max.crafting.interpreter.jlox.ast.Grouping;
import org.max.crafting.interpreter.jlox.ast.LogicalExpression;
import org.max.crafting.interpreter.jlox.ast.UnaryExpression;
import org.max.crafting.interpreter.jlox.ast.VariableExpression;

public interface ExpressionVisitor {

    Object visitBinaryExpression(BinaryExpression binaryExp);

    Object visitUnaryExpression(UnaryExpression unaryExp);

    Object visitLiteral(Literal literal);

    Object visitParentheses(Grouping grouping);

    Object visitVariableExpression(VariableExpression varExpr);

    Object visitAssignmentExpression(Assignment assignment);

    Object visitCommaExpression(CommaExpresssion commaExpr);

    Object visitLogicalExpression(LogicalExpression logicalExpr);
}
