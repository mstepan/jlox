package org.max.crafting.interpreter.jlox.interpreter;

import org.max.crafting.interpreter.jlox.Lox;
import org.max.crafting.interpreter.jlox.ast.Assignment;
import org.max.crafting.interpreter.jlox.ast.BinaryExpression;
import org.max.crafting.interpreter.jlox.ast.Block;
import org.max.crafting.interpreter.jlox.ast.CommaExpresssion;
import org.max.crafting.interpreter.jlox.ast.ExpressionStmt;
import org.max.crafting.interpreter.jlox.ast.Grouping;
import org.max.crafting.interpreter.jlox.ast.Literal;
import org.max.crafting.interpreter.jlox.ast.PrintStmt;
import org.max.crafting.interpreter.jlox.ast.Stmt;
import org.max.crafting.interpreter.jlox.ast.UnaryExpression;
import org.max.crafting.interpreter.jlox.ast.VarStmt;
import org.max.crafting.interpreter.jlox.ast.VariableExpression;
import org.max.crafting.interpreter.jlox.model.Token;

import java.util.List;

/**
 * Traverse Abstract Syntax Tree in post-order and evaluate nodes.
 */
public class Interpreter implements ExpressionVisitor, StmtVisitor<Void> {

    private final Environment environment = new Environment();

    public void clearState() {
        environment.clear();
    }

    public void interpret(List<Stmt> statements) {
        try {
            for (Stmt singleStmt : statements) {
                execute(singleStmt);
            }
        }
        catch (RuntimeError ex) {
            Lox.runtimeError(ex);
        }
    }

    private void execute(Stmt singleStmt) {
        singleStmt.accept(this);
    }

    @Override
    public Void visitVarStmt(VarStmt stmt) {

        Object val = null;

        if (stmt.initExpr != null) {
            val = stmt.initExpr.accept(this);
        }
        environment.define(stmt.name.lexeme, val);

        return null;
    }

    @Override
    public Void visitExpressionStmt(ExpressionStmt stmt) {
        stmt.expr.accept(this);
        return null;
    }

    @Override
    public Void visitPrintStmt(PrintStmt stmt) {
        Object value = stmt.expr.accept(this);
        System.out.println(stringify(value));
        return null;
    }

    @Override
    public Void visitBlockStatement(Block blockStmt) {
        executeBlock(blockStmt);
        return null;
    }

    private void executeBlock(Block blockStmt) {
        try (Environment.Scope notUsed = environment.newScope()) {
            for (Stmt innerStmt : blockStmt.statements) {
                innerStmt.accept(this);
            }
        }
    }

    @Override
    public Object visitCommaExpression(CommaExpresssion commaExpr) {
        // IMPORTANT: left side evaluated, but returned value ignored
        // this is important for assignments if any
        commaExpr.left.accept(this);
        return commaExpr.right.accept(this);
    }

    @Override
    public Object visitAssignmentExpression(Assignment assignment) {
        Object value = assignment.value.accept(this);
        environment.assign(assignment.name, value);
        return value;
    }

    @Override
    public Object visitBinaryExpression(BinaryExpression binaryExp) {

        Object left = binaryExp.left.accept(this);
        Object right = binaryExp.right.accept(this);

        return switch (binaryExp.operator.type) {
            case MINUS -> {
                checkBothOperandsNumbers(binaryExp.operator, left, right);
                yield minus(binaryExp.operator, left, right);
            }
            case STAR -> {
                checkBothOperandsNumbers(binaryExp.operator, left, right);
                yield multiply(binaryExp.operator, left, right);
            }
            case SLASH -> {
                checkBothOperandsNumbers(binaryExp.operator, left, right);
                yield divide(binaryExp.operator, left, right);
            }
            case PLUS -> plus(binaryExp.operator, left, right);
            case GREATER -> {
                checkBothOperandsNumbers(binaryExp.operator, left, right);
                yield toDouble(binaryExp.operator, left) > toDouble(binaryExp.operator, right);
            }
            case GREATER_EQUAL -> {
                checkBothOperandsNumbers(binaryExp.operator, left, right);
                yield toDouble(binaryExp.operator, left) >= toDouble(binaryExp.operator, right);
            }
            case LESS -> {
                checkBothOperandsNumbers(binaryExp.operator, left, right);
                yield toDouble(binaryExp.operator, left) < toDouble(binaryExp.operator, right);
            }
            case LESS_EQUAL -> {
                checkBothOperandsNumbers(binaryExp.operator, left, right);
                yield toDouble(binaryExp.operator, left) <= toDouble(binaryExp.operator, right);
            }
            case EQUAL_EQUAL -> {
                yield isEqual(binaryExp.operator, left, right);
            }
            case BANG_EQUAL -> {
                yield (!isEqual(binaryExp.operator, left, right));
            }
            case COMMA -> {
                // for comma, we just evaluate and discard left hand side and use only right side
                yield right;
            }
            default -> throw new IllegalStateException("Unsupported expression: " + binaryExp.operator.type);
        };
    }

    private Object plus(Token operator, Object left, Object right) {
        if (isInt(left) && isInt(right)) {
            return (int) left + (int) right;
        }
        if (isNumber(left) && isNumber(right)) {
            return toDouble(operator, left) + toDouble(operator, right);
        }

        // below code allows the following cases:
        // string + number, string + boolean, string + nil etc.
        // number + string, boolean + string etc.
        if (left instanceof String || right instanceof String) {
            return stringify(left) + stringify(right);
        }

        throw new RuntimeError(operator, "Both operands must be numbers or one should be a string.");
    }

    private Object minus(Token operator, Object left, Object right) {
        if (isInt(left) && isInt(right)) {
            return (int) left - (int) right;
        }

        return toDouble(operator, left) - toDouble(operator, right);
    }

    private Object multiply(Token operator, Object left, Object right) {
        if (isInt(left) && isInt(right)) {
            return (int) left * (int) right;
        }

        return toDouble(operator, left) * toDouble(operator, right);
    }

    private Object divide(Token operator, Object left, Object right) {
        if (isInt(left) && isInt(right)) {
            final int rightInt = (int) right;
            if (rightInt == 0) {
                throw new RuntimeError(operator, "Integer division by zero.");
            }
            return (int) left / rightInt;
        }

        return toDouble(operator, left) / toDouble(operator, right);
    }

    private static double toDouble(Token operator, Object value) {
        if (value instanceof Double) {
            return (double) value;
        }
        if (value instanceof Integer) {
            return (double) ((int) value);
        }

        throw new RuntimeError(operator, "Unsupported type: " + value.getClass().getCanonicalName());
    }

    private boolean isEqual(Token operator, Object left, Object right) {
        if (left == null && right == null) {
            return true;
        }
        if (left == null) {
            return false;
        }

        /*
         * According to IEEE-754 standard:
         * 1. 0.0 / 0.0 = NaN
         * 2. (NaN == NaN) => false (in java NaN.equals(NaN) returns true,
         * so we will use '==' to preserve standard).
         */
        if (isNumber(left) && isNumber(right)) {
            return toDouble(operator, left) == toDouble(operator, right);
        }

        return left.equals(right);
    }

    @Override
    public Object visitUnaryExpression(UnaryExpression unaryExp) {
        Object res = unaryExp.expression.accept(this);

        return switch (unaryExp.operation.type) {
            case MINUS -> {
                checkNumberOperand(unaryExp.operation, res);
                yield negate(res);
            }
            case BANG -> !isTrue(res);
            default -> null;
        };
    }

    private Object negate(Object value) {
        if (isInt(value)) {
            return -(int) value;
        }

        return -(double) value;
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

    @Override
    public Object visitVariableExpression(VariableExpression varExpr) {
        return environment.get(varExpr.name);
    }

    private static String stringify(Object value) {
        if (value == null) {
            return "nil";
        }

        return value.toString();
    }

    private void checkBothOperandsNumbers(Token operator, Object left, Object right) {
        if (isNumber(left) && isNumber(right)) {
            return;
        }
        throw new RuntimeError(operator, "Both operands must be numbers.");
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (isNumber(operand)) {
            return;
        }
        throw new RuntimeError(operator, "Operand must be a number.");
    }

    private static boolean isNumber(Object value) {
        return isDouble(value) || isInt(value);
    }

    private static boolean isInt(Object value) {
        return value instanceof Integer;
    }

    private static boolean isDouble(Object value) {
        return value instanceof Double;
    }

    // Runtime error during AST evaluation
    public static final class RuntimeError extends RuntimeException {

        public final Token operator;

        RuntimeError(Token operator, String errorMsg) {
            super(errorMsg);
            this.operator = operator;
        }
    }
}
