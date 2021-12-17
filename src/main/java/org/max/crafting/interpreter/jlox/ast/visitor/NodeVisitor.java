package org.max.crafting.interpreter.jlox.ast.visitor;

import org.max.crafting.interpreter.jlox.ast.BinaryExpression;
import org.max.crafting.interpreter.jlox.ast.Literal;
import org.max.crafting.interpreter.jlox.ast.Grouping;
import org.max.crafting.interpreter.jlox.ast.UnaryExpression;

public interface NodeVisitor {

    Object visitBinaryExpression(BinaryExpression binaryExp);

    Object visitUnaryExpression(UnaryExpression unaryExp);

    Object visitLiteral(Literal literal);

    Object visitParentheses(Grouping grouping);
}
