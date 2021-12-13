package org.max.crafting.interpreter.jlox;

import org.max.crafting.interpreter.jlox.ast.Expression;
import org.max.crafting.interpreter.jlox.ast.visitor.PrettyPrinterNodeVisitor;
import org.max.crafting.interpreter.jlox.lexer.Lexer;
import org.max.crafting.interpreter.jlox.parser.RecursiveDescentParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Lox {

    private static boolean hadError = false;

    public static void main(String[] args) throws IOException {
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

    public static void error(int lineNumber, String errorMsg) {
        System.err.println(String.format("[line %d] Error: %s", lineNumber, errorMsg));
        hadError = true;
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
                hadError = false;
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
            run(scriptAsStr);
        }
        catch (IOException ioEx) {
            System.err.println("Can't read file specified by path: " + scriptFile);
            System.exit(1);
        }
    }

    private static void run(String source) {

        // scanner, lexer step
        final Lexer lexer = new Lexer(source);

        // parser step
        final RecursiveDescentParser parser = new RecursiveDescentParser(lexer.tokenize());

        // abstract syntax tree
        Expression expr = parser.createAst();

        // some debugging
        PrettyPrinterNodeVisitor prettyPrinterVisitor = new PrettyPrinterNodeVisitor();

        expr.accept(prettyPrinterVisitor);

        System.out.println(prettyPrinterVisitor.getRepresentation());
    }

}
