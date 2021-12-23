package org.max.crafting.interpreter.jlox.ast.visitor;

import org.max.crafting.interpreter.jlox.Lox;
import org.max.crafting.interpreter.jlox.ast.BinaryExpression;
import org.max.crafting.interpreter.jlox.ast.Expression;
import org.max.crafting.interpreter.jlox.ast.Grouping;
import org.max.crafting.interpreter.jlox.ast.Literal;
import org.max.crafting.interpreter.jlox.ast.UnaryExpression;
import org.max.crafting.interpreter.jlox.model.Token;

/**
 * Traverse Abstract Syntax Tree in post-order and evaluate nodes.
 */
public class Interpreter implements NodeVisitor {

    public String interpret(Expression expression) {
        try {
            return stringify(expression.accept(this));
        }
        catch (RuntimeError ex) {
            Lox.runtimeError(ex);
        }
        return null;
    }

    @Override
    public Object visitBinaryExpression(BinaryExpression binaryExp) {

        Object left = binaryExp.left.accept(this);
        Object right = binaryExp.right.accept(this);

        return switch (binaryExp.operator.type) {
            case MINUS -> {
                checkBothOperandsNumbers(binaryExp.operator, left, right);
                yield (double) left - (double) right;
            }
            case STAR -> {
                checkBothOperandsNumbers(binaryExp.operator, left, right);
                yield (double) left * (double) right;
            }
            case SLASH -> {
                checkBothOperandsNumbers(binaryExp.operator, left, right);
                yield (double) left / (double) right;
            }
            case PLUS -> plus(binaryExp.operator, left, right);
            case GREATER -> {
                checkBothOperandsNumbers(binaryExp.operator, left, right);
                yield (double) left > (double) right;
            }
            case GREATER_EQUAL -> {
                checkBothOperandsNumbers(binaryExp.operator, left, right);
                yield (double) left >= (double) right;
            }
            case LESS -> {
                checkBothOperandsNumbers(binaryExp.operator, left, right);
                yield (double) left < (double) right;
            }
            case LESS_EQUAL -> {
                checkBothOperandsNumbers(binaryExp.operator, left, right);
                yield (double) left <= (double) right;
            }
            case EQUAL_EQUAL -> {
                checkBothOperandsNumbers(binaryExp.operator, left, right);
                yield isEqual(left, right);
            }
            case BANG_EQUAL -> {
                checkBothOperandsNumbers(binaryExp.operator, left, right);
                yield (!isEqual(left, right));
            }
            case COMMA -> {
                // for comma, we just evaluate and discard left hand side and use only right side
                yield right;
            }
            default -> throw new IllegalStateException("Unsupported expression: " + binaryExp.operator.type);
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

    private Object plus(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) {
            return (double) left + (double) right;
        }
        // below code allows the following cases:
        // string + number, string + boolean, string + nil etc.
        // number + string, boolean + string etc.
        if (left instanceof String || right instanceof String) {
            return stringify(left) + stringify(right);
        }

        throw new RuntimeError(operator, "Operands must be two numbers or one should be string.");
    }

    @Override
    public Object visitUnaryExpression(UnaryExpression unaryExp) {
        Object res = unaryExp.expression.accept(this);

        return switch (unaryExp.operation.type) {
            case MINUS -> {
                checkNumberOperand(unaryExp.operation, res);
                yield -(double) res;
            }
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

    private static String stringify(Object value) {
        if (value == null) {
            return "nil";
        }

        if (value instanceof Double) {
            String doubleValue = String.valueOf(value);
            if (doubleValue.endsWith(".0")) {
                return doubleValue.substring(0, doubleValue.indexOf(".0"));
            }

            return doubleValue;
        }

        return value.toString();
    }

    private void checkBothOperandsNumbers(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) {
            return;
        }
        throw new RuntimeError(operator, "Both operands must be numbers.");
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) {
            return;
        }
        throw new RuntimeError(operator, "Operand must be a number.");
    }

    public static final class RuntimeError extends RuntimeException {

        public final Token operator;

        RuntimeError(Token operator, String errorMsg) {
            super(errorMsg);
            this.operator = operator;
        }
    }
}
