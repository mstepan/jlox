package org.max.crafting.interpreter.jlox.interpreter;

import org.max.crafting.interpreter.jlox.ast.BinaryExpression;
import org.max.crafting.interpreter.jlox.ast.Grouping;
import org.max.crafting.interpreter.jlox.ast.Literal;
import org.max.crafting.interpreter.jlox.ast.UnaryExpression;


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
    public Object visitParentheses(Grouping grouping) {
        return "(" + grouping.expression.accept(this) + ")";
    }

    @Override
    public Object visitLiteral(Literal literal) {
        return literal.value;
    }

}
