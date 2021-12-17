package org.max.crafting.interpreter.jlox.ast.visitor;

import org.max.crafting.interpreter.jlox.ast.BinaryExpression;
import org.max.crafting.interpreter.jlox.ast.Grouping;
import org.max.crafting.interpreter.jlox.ast.Literal;
import org.max.crafting.interpreter.jlox.ast.UnaryExpression;


public class PrettyPrinterNodeVisitor implements NodeVisitor {

    @Override
    public Object visitBinaryExpression(BinaryExpression binaryExp) {
        String leftStr = String.valueOf(binaryExp.getLeft().accept(this));
        String opStr = binaryExp.getOperation().getType().getName();
        String rightStr = String.valueOf(binaryExp.getRight().accept(this));
        return leftStr + " " + opStr + " " + rightStr;
    }

    @Override
    public Object visitUnaryExpression(UnaryExpression unaryExp) {
        String opStr = unaryExp.getOperation().getType().getName();
        return opStr + unaryExp.getExpression().accept(this);
    }

    @Override
    public Object visitParentheses(Grouping grouping) {
        return "(" + grouping.getExpression().accept(this) + ")";
    }

    @Override
    public Object visitLiteral(Literal literal) {
        return literal.getValue();
    }

}
