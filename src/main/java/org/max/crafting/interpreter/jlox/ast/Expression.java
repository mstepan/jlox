package org.max.crafting.interpreter.jlox.ast;

import org.max.crafting.interpreter.jlox.interpreter.ExpressionVisitor;

public interface Expression {

    Object accept(ExpressionVisitor visitor);

}
