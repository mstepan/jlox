package org.max.crafting.interpreter.jlox.ast.visitor;

import org.max.crafting.interpreter.jlox.ast.BinaryExpression;
import org.max.crafting.interpreter.jlox.ast.Grouping;
import org.max.crafting.interpreter.jlox.ast.Literal;
import org.max.crafting.interpreter.jlox.ast.UnaryExpression;

public class Interpreter implements NodeVisitor {

    @Override
    public Object visitBinaryExpression(BinaryExpression binaryExp) {

        return switch (binaryExp.operation.type) {
            case MINUS -> (double) binaryExp.left.accept(this) - (double) binaryExp.right.accept(this);
            case STAR -> (double) binaryExp.left.accept(this) * (double) binaryExp.right.accept(this);
            case SLASH -> (double) binaryExp.left.accept(this) / (double) binaryExp.right.accept(this);
            case PLUS -> plus(binaryExp.left.accept(this), binaryExp.right.accept(this));
            case GREATER -> (double) binaryExp.left.accept(this) > (double) binaryExp.right.accept(this);
            case GREATER_EQUAL -> (double) binaryExp.left.accept(this) >= (double) binaryExp.right.accept(this);
            case LESS -> (double) binaryExp.left.accept(this) < (double) binaryExp.right.accept(this);
            case LESS_EQUAL -> (double) binaryExp.left.accept(this) <= (double) binaryExp.right.accept(this);
            case EQUAL_EQUAL -> isEqual(binaryExp.left.accept(this), binaryExp.right.accept(this));
            case BANG_EQUAL -> !isEqual(binaryExp.left.accept(this), binaryExp.right.accept(this));
            case COMMA -> {
                // for comma, we just discard left hand side
                Object leftRes = binaryExp.left.accept(this);

                Object rightRes = binaryExp.right.accept(this);

                yield binaryExp.right.accept(this);
            }
            default -> throw new IllegalStateException("Unsupported expression: " + binaryExp.operation.type);
        };
    }

    private boolean isEqual(Object left, Object right) {
        if (left == null && right == null) {
            return true;
        }
        if (left == null) {
            return false;
        }
        return left.equals(right);
    }

    private Object plus(Object left, Object right) {
        if (left instanceof Double && right instanceof Double) {
            return (double) left + (double) right;
        }
        if (left instanceof String && right instanceof String) {
            return String.valueOf(left) + right;
        }

        //TODO: do something here if types are not compatible
        return null;
    }

    @Override
    public Object visitUnaryExpression(UnaryExpression unaryExp) {
        Object res = unaryExp.expression.accept(this);

        return switch (unaryExp.operation.type) {
            case MINUS -> -(double) res;
            case BANG -> !isTrue(res);
            default -> null;
        };
    }

    /**
     * We will follow Ruby rules here:
     * 1. null -> false
     * 2. Boolean -> value itself
     * 3. everything else -> true
     */
    private static boolean isTrue(Object value) {
        if (value == null) {
            return false;
        }

        if (value instanceof Boolean) {
            return (boolean) value;
        }

        return true;
    }

    @Override
    public Object visitLiteral(Literal literal) {
        return literal.value;
    }

    @Override
    public Object visitParentheses(Grouping grouping) {
        return grouping.expression.accept(this);
    }
}
