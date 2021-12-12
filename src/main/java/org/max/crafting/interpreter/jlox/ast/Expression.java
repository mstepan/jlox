package org.max.crafting.interpreter.jlox.ast;

import org.max.crafting.interpreter.jlox.ast.visitor.NodeVisitor;

public interface Expression {

    void accept(NodeVisitor visitor);

}
