package org.max.crafting.interpreter.jlox.interpreter;

import org.max.crafting.interpreter.jlox.ast.Assignment;
import org.max.crafting.interpreter.jlox.ast.BinaryExpression;
import org.max.crafting.interpreter.jlox.ast.CommaExpresssion;
import org.max.crafting.interpreter.jlox.ast.Grouping;
import org.max.crafting.interpreter.jlox.ast.Literal;
import org.max.crafting.interpreter.jlox.ast.UnaryExpression;
import org.max.crafting.interpreter.jlox.ast.VariableExpression;


public class PrettyPrinterExpressionVisitor implements ExpressionVisitor {

    @Override
    public Object visitBinaryExpression(BinaryExpression binaryExp) {
        String leftStr = String.valueOf(binaryExp.left.accept(this));
        String opStr = binaryExp.operator.type.name;
        String rightStr = String.valueOf(binaryExp.right.accept(this));
        return leftStr + " " + opStr + " " + rightStr;
    }

    @Override
    public Object visitUnaryExpression(UnaryExpression unaryExp) {
        String opStr = unaryExp.operation.type.name;
        return opStr + unaryExp.expression.accept(this);
    }

    @Override
    public Object visitVariableExpression(VariableExpression varExpr) {
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
    public Object visitCommaExpression(CommaExpresssion commaExpr) {
        return commaExpr.right.accept(this);
    }
}
