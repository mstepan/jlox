package org.max.crafting.interpreter.jlox.interpreter;

import org.max.crafting.interpreter.jlox.ast.Assignment;
import org.max.crafting.interpreter.jlox.ast.BinaryExpr;
import org.max.crafting.interpreter.jlox.ast.CallExpr;
import org.max.crafting.interpreter.jlox.ast.CommaExpr;
import org.max.crafting.interpreter.jlox.ast.FunctionExpr;
import org.max.crafting.interpreter.jlox.ast.Grouping;
import org.max.crafting.interpreter.jlox.ast.Literal;
import org.max.crafting.interpreter.jlox.ast.LogicalExpr;
import org.max.crafting.interpreter.jlox.ast.UnaryExpr;
import org.max.crafting.interpreter.jlox.ast.VariableExpr;


public class PrettyPrinterExpressionVisitor implements ExpressionVisitor {

    @Override
    public Object visitBinaryExpression(BinaryExpr binaryExp) {
        String leftStr = String.valueOf(binaryExp.left.accept(this));
        String opStr = binaryExp.operator.type.name;
        String rightStr = String.valueOf(binaryExp.right.accept(this));
        return leftStr + " " + opStr + " " + rightStr;
    }

    @Override
    public Object visitUnaryExpression(UnaryExpr unaryExp) {
        String opStr = unaryExp.operation.type.name;
        return opStr + unaryExp.expression.accept(this);
    }

    @Override
    public Object visitVariableExpression(VariableExpr varExpr) {
        return varExpr.name;
    }

    @Override
    public Object visitParentheses(Grouping grouping) {
        return "(" + grouping.expression.accept(this) + ")";
    }

    @Override
    public Object visitLiteral(Literal literal) {
        return literal.value;
    }

    @Override
    public Object visitAssignmentExpression(Assignment assignment) {
        return assignment.name + " = " + assignment.value.accept(this);
    }

    @Override
    public Object visitCommaExpression(CommaExpr commaExpr) {
        return commaExpr.right.accept(this);
    }

    @Override
    public Object visitLogicalExpression(LogicalExpr logicalExpr) {
        return String.valueOf(logicalExpr.left.accept(this)) + logicalExpr.operator + logicalExpr.right.accept(this);
    }

    @Override
    public Object visitCall(CallExpr callExpr) {
        return "call " + callExpr.callee.accept(this);
    }

    @Override
    public Object visitFunctionExpression(FunctionExpr functionExpr) {
        return String.valueOf("lambda function: " + functionExpr.toString());
    }
}
