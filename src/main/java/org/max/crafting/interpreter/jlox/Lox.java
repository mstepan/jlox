package org.max.crafting.interpreter.jlox;

import org.max.crafting.interpreter.jlox.ast.Stmt;
import org.max.crafting.interpreter.jlox.interpreter.Interpreter;
import org.max.crafting.interpreter.jlox.lexer.Lexer;
import org.max.crafting.interpreter.jlox.model.Token;
import org.max.crafting.interpreter.jlox.parser.RecursiveDescentParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Lox {

    private static boolean hadSyntaxError;
    private static boolean hadRuntimeError;

    static String lastErrorMsg;

    private static final Interpreter interpreter = new Interpreter();

    public static void main(String[] args) {
        // running in REPL mode
        if (args.length == 0) {
            runReplMode();
        }
        // running some script
        else if (args.length == 1) {
            runFile(args[0]);
        }
        else {
            System.out.println("Usage: jlox [script]");
            System.exit(1);
        }
    }

    /**
     * Run interpreter in REPL mode
     */
    private static void runReplMode() {
        try (InputStreamReader inStream = new InputStreamReader(System.in);
             BufferedReader reader = new BufferedReader(inStream)) {
            while (true) {
                System.out.println(">");
                String lineToEval = reader.readLine();
                // Ctrl + D pressed
                if (lineToEval == null) {
                    break;
                }
                run(lineToEval);
                hadSyntaxError = false;
            }

        }
        catch (IOException ioEx) {
            System.err.println("Error occurred " + ioEx.getMessage());
        }
    }

    private static void runFile(String filePath) {
        Path scriptFile = Paths.get(filePath);

        try {
            String scriptAsStr = Files.readString(scriptFile, Charset.defaultCharset());
            runScript(scriptAsStr);
            if (hadSyntaxError) {
                System.exit(65);
            }
            if (hadRuntimeError) {
                System.exit(70);
            }
        }
        catch (IOException ioEx) {
            System.err.println("Can't read file specified by path: " + scriptFile);
            System.exit(1);
        }
    }

    /**
     * Execute program.
     */
    static void runScript(String source) {
        clearState();
        run(source);
    }

    /**
     * Execute single script or single statement from EVAL.
     */
    static void run(String source) {

        // scanner, lexer step
        final Lexer lexer = new Lexer(source);

        List<Token> tokens = lexer.tokenize();

        // parser step
        final RecursiveDescentParser parser = new RecursiveDescentParser(tokens);

        // abstract syntax tree
        List<Stmt> statements = parser.parse();

        if (hasError()) {
            return;
        }

        // evaluate AST
        interpreter.interpret(statements);
    }

    private static void clearState() {
        interpreter.clearState();
        hadSyntaxError = false;
        hadRuntimeError = false;
        lastErrorMsg = null;
    }

    public static boolean hasError() {
        return hadSyntaxError || hadRuntimeError;
    }

    /**
     * Report syntax (static) error from lexer.
     */
    public static void error(int lineNumber, String errorMsg) {
        lastErrorMsg = String.format("[line %d] %s", lineNumber, errorMsg);
        System.err.println(lastErrorMsg);
        hadSyntaxError = true;
    }

    /**
     * Report dynamic (runtime) error from parser evaluation.
     */
    public static void runtimeError(Interpreter.RuntimeError error) {
        lastErrorMsg = String.format("[line %s]: %s", error.operator.lineNumber, error.getMessage());
        System.err.println(lastErrorMsg);
        hadRuntimeError = true;
    }
}
