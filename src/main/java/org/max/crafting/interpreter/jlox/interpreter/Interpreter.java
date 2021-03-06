package org.max.crafting.interpreter.jlox.interpreter;

import org.max.crafting.interpreter.jlox.Lox;
import org.max.crafting.interpreter.jlox.ast.Assignment;
import org.max.crafting.interpreter.jlox.ast.BinaryExpr;
import org.max.crafting.interpreter.jlox.ast.Block;
import org.max.crafting.interpreter.jlox.ast.CallExpr;
import org.max.crafting.interpreter.jlox.ast.CommaExpr;
import org.max.crafting.interpreter.jlox.ast.Expression;
import org.max.crafting.interpreter.jlox.ast.ExpressionStmt;
import org.max.crafting.interpreter.jlox.ast.FunctionExpr;
import org.max.crafting.interpreter.jlox.ast.FunctionStmt;
import org.max.crafting.interpreter.jlox.ast.Grouping;
import org.max.crafting.interpreter.jlox.ast.IfStmt;
import org.max.crafting.interpreter.jlox.ast.Literal;
import org.max.crafting.interpreter.jlox.ast.LogicalExpr;
import org.max.crafting.interpreter.jlox.ast.ReturnStmt;
import org.max.crafting.interpreter.jlox.ast.Stmt;
import org.max.crafting.interpreter.jlox.ast.UnaryExpr;
import org.max.crafting.interpreter.jlox.ast.VarStmt;
import org.max.crafting.interpreter.jlox.ast.VariableExpr;
import org.max.crafting.interpreter.jlox.ast.WhileStmt;
import org.max.crafting.interpreter.jlox.interpreter.global.ClockFunction;
import org.max.crafting.interpreter.jlox.interpreter.global.MaxFunction;
import org.max.crafting.interpreter.jlox.interpreter.global.PrintFunction;
import org.max.crafting.interpreter.jlox.interpreter.global.SleepFunction;
import org.max.crafting.interpreter.jlox.model.Token;
import org.max.crafting.interpreter.jlox.model.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Traverse Abstract Syntax Tree in post-order and evaluate nodes.
 */
public class Interpreter implements ExpressionVisitor, StmtVisitor<Void> {

    /**
     * Store all globally visible values.
     */
    public final Environment globals = new Environment();

    /**
     * Store all context dependant values.
     */
    private Environment environment = globals;

    public Interpreter() {
        // define all native functions in global scope
        globals.defineInPlace("print", new PrintFunction());
        globals.defineInPlace("max", new MaxFunction());
        globals.defineInPlace("clock", new ClockFunction());
        globals.defineInPlace("sleep", new SleepFunction());
    }

    public void interpret(List<Stmt> statements) {
        try {
            for (Stmt singleStmt : statements) {
                execute(singleStmt);
            }
        }
        catch (RuntimeInterpreterException ex) {
            Lox.runtimeError(ex);
        }
    }

    public String interpret(Expression expr) {
        try {
            return stringify(eval(expr));
        }
        catch (RuntimeInterpreterException ex) {
            Lox.runtimeError(ex);
        }
        return stringify(null);
    }

    /**
     * Execute single statement.
     */
    private void execute(Stmt singleStmt) {
        singleStmt.accept(this);
    }

    /**
     * Evaluate single expression.
     */
    private Object eval(Expression expr) {
        return expr.accept(this);
    }

    @Override
    public Void visitFunction(FunctionStmt fnStmt) {
        final LoxFunction function = new LoxFunction(fnStmt, environment);
        environment.defineInPlace(fnStmt.name.lexeme, function);
        return null;
    }

    @Override
    public Object visitFunctionExpression(FunctionExpr functionExpr) {
        return new LoxFunction(functionExpr, environment);
    }

    @Override
    public Void visitVarStmt(VarStmt stmt) {

        Object val = null;

        if (stmt.initExpr != null) {
            val = eval(stmt.initExpr);
        }

        //Need to create a new Environment here, to remove any closure leakage
        environment  = environment.define(stmt.name.lexeme, val);

        return null;
    }

    @Override
    public Void visitExpressionStmt(ExpressionStmt stmt) {
        eval(stmt.expr);
        return null;
    }

    @Override
    public Void visitBlockStatement(Block blockStmt) {
        executeBlock(blockStmt);
        return null;
    }

    /**
     * Create new scope and execute list of statements within this new scope.
     */
    void executeBlock(Block blockStmt) {
        executeStatements(blockStmt.statements, new Environment(environment));
    }

    /**
     * Execute list of statements using provided scope.
     */
    void executeStatements(List<Stmt> statements, Environment curScope) {
        final Environment prev = environment;
        environment = curScope;

        try {
            for (Stmt singleStmt : statements) {
                singleStmt.accept(this);
            }
        }
        finally {
            environment = prev;
        }
    }

    @Override
    public Void visitIfStatement(IfStmt ifStmt) {

        boolean conditionTrue = isTrue(eval(ifStmt.condition));

        if (conditionTrue) {
            return ifStmt.thenBranch.accept(this);
        }
        else if (ifStmt.elseBranch != null) {
            return ifStmt.elseBranch.accept(this);
        }

        return null;
    }

    @Override
    public Void visitWhileStatement(WhileStmt whileStmt) {
        while (isTrue(eval(whileStmt.condition))) {
            whileStmt.body.accept(this);
        }

        return null;
    }

    @Override
    public Void visitReturnStmt(ReturnStmt returnStmt) {
        Object value = (returnStmt.expr == null) ? null : eval(returnStmt.expr);
        throw new Return(value);
    }

    @Override
    public Object visitCommaExpression(CommaExpr commaExpr) {
        // IMPORTANT: left side evaluated, but returned value ignored
        // this is important for assignments if any
        eval(commaExpr.left);
        return eval(commaExpr.right);
    }

    @Override
    public Object visitAssignmentExpression(Assignment assignment) {
        Object value = eval(assignment.value);

        //I don't need to create new Environment, assignment should always change current Environment
        environment.assign(assignment.name, value);
        return value;
    }

    @Override
    public Object visitLogicalExpression(LogicalExpr logicalExpr) {

        Object left = eval(logicalExpr.left);

        // short-circuit AND
        if ((logicalExpr.operator.type == TokenType.AND) && isFalse(left)) {
            return left;
        }

        // short-circuit OR
        if ((logicalExpr.operator.type == TokenType.OR) && isTrue(left)) {
            return left;
        }

        return eval(logicalExpr.right);
    }

    @Override
    public Object visitBinaryExpression(BinaryExpr binaryExp) {

        Object left = eval(binaryExp.left);
        Object right = eval(binaryExp.right);

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

        throw new RuntimeInterpreterException(operator, "Both operands must be numbers or one should be a string.");
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
                throw new RuntimeInterpreterException(operator, "Integer division by zero.");
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

        throw new RuntimeInterpreterException(operator, "Unsupported type: " + value.getClass().getCanonicalName());
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
    public Object visitUnaryExpression(UnaryExpr unaryExp) {
        Object res = eval(unaryExp.expression);

        return switch (unaryExp.operation.type) {
            case MINUS -> {
                checkNumberOperand(unaryExp.operation, res);
                yield negate(res);
            }
            case BANG -> isFalse(res);
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
     * 1. 'null' is always false
     * 2. 'boolean' value evaluated to itself
     * 3. everything else treated as 'true'
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

    private static boolean isFalse(Object value) {
        return !isTrue(value);
    }

    @Override
    public Object visitCall(CallExpr callExpr) {

        Object callee = eval(callExpr.callee);

        if (!(callee instanceof JLoxCallable)) {
            throw new RuntimeInterpreterException(callExpr.rightParen, "Can only call function or class.");
        }

        List<Object> arguments = new ArrayList<>();

        for (Expression singleArg : callExpr.arguments) {
            arguments.add(eval(singleArg));
        }

        JLoxCallable func = (JLoxCallable) callee;

        // check parameters count declared in function actually corresponds to passed arguments count
        // better to use Python like behaviour here, just fail if parameters count != arguments count
        if (func.arity() != arguments.size()) {
            throw new RuntimeInterpreterException(callExpr.rightParen,
                                                  String.format("Expected %d parameters for %s, but passed %d.",
                                                                func.arity(), func.toString(), arguments.size()));
        }

        return func.call(this, arguments);
    }

    @Override
    public Object visitLiteral(Literal literal) {
        return literal.value;
    }

    @Override
    public Object visitParentheses(Grouping grouping) {
        return eval(grouping.expression);
    }

    @Override
    public Object visitVariableExpression(VariableExpr varExpr) {
        return environment.get(varExpr.name);
    }

    public static String stringify(Object value) {
        if (value == null) {
            return "nil";
        }

        return value.toString();
    }

    private void checkBothOperandsNumbers(Token operator, Object left, Object right) {
        if (isNumber(left) && isNumber(right)) {
            return;
        }
        throw new RuntimeInterpreterException(operator, "Both operands must be numbers.");
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (isNumber(operand)) {
            return;
        }
        throw new RuntimeInterpreterException(operator, "Operand must be a number.");
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
}
