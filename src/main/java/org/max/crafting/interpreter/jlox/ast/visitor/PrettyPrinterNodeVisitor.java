package org.max.crafting.interpreter.jlox.ast.visitor;

import org.max.crafting.interpreter.jlox.ast.BinaryExpression;
import org.max.crafting.interpreter.jlox.ast.Literal;
import org.max.crafting.interpreter.jlox.ast.Parentheses;
import org.max.crafting.interpreter.jlox.ast.UnaryExpression;


public class PrettyPrinterNodeVisitor implements NodeVisitor {

    private final StringBuilder buf;

    public PrettyPrinterNodeVisitor() {
        buf = new StringBuilder();
    }

    public String getRepresentation() {
        return buf.toString();
    }

    @Override
    public void visitBinaryExpression(BinaryExpression binaryExp) {
        binaryExp.getLeft().accept(this);
        buf.append(" ").append(binaryExp.getOperation().getType().getName()).append(" ");
        binaryExp.getRight().accept(this);
    }

    @Override
    public void visitUnaryExpression(UnaryExpression unaryExp) {
        buf.append(unaryExp.getOperation().getType().getName());
        unaryExp.getExpression().accept(this);
    }

    @Override
    public void visitParentheses(Parentheses parentheses) {
        buf.append("( ");
        parentheses.getExpression().accept(this);
        buf.append(" )");
    }

    @Override
    public void visitLiteral(Literal literal) {
        buf.append(literal.getValue());
    }

}
