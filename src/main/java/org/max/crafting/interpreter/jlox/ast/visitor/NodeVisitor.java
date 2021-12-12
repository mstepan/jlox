package org.max.crafting.interpreter.jlox.ast.visitor;

import org.max.crafting.interpreter.jlox.ast.BinaryExpression;
import org.max.crafting.interpreter.jlox.ast.Literal;
import org.max.crafting.interpreter.jlox.ast.Parentheses;
import org.max.crafting.interpreter.jlox.ast.UnaryExpression;

public interface NodeVisitor {

    void visitBinaryExpression(BinaryExpression binaryExp);

    void visitUnaryExpression(UnaryExpression unaryExp);

    void visitLiteral(Literal literal);

    void visitParentheses(Parentheses parentheses);
}
