package org.max.crafting.interpreter.jlox.ast.visitor;

import org.max.crafting.interpreter.jlox.ast.BinaryExpression;
import org.max.crafting.interpreter.jlox.ast.Grouping;
import org.max.crafting.interpreter.jlox.ast.Literal;
import org.max.crafting.interpreter.jlox.ast.UnaryExpression;

public class Interpreter implements NodeVisitor {

    @Override
    public Object visitBinaryExpression(BinaryExpression binaryExp) {
        return null;
    }

    @Override
    public Object visitUnaryExpression(UnaryExpression unaryExp) {
        return null;
    }

    @Override
    public Object visitLiteral(Literal literal) {
        return null;
    }

    @Override
    public Object visitParentheses(Grouping grouping) {
        return null;
    }
}
