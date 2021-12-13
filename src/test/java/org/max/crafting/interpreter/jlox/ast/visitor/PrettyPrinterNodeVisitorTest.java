package org.max.crafting.interpreter.jlox.ast.visitor;

import org.junit.jupiter.api.Test;
import org.max.crafting.interpreter.jlox.ast.BinaryExpression;
import org.max.crafting.interpreter.jlox.ast.Expression;
import org.max.crafting.interpreter.jlox.ast.Literal;
import org.max.crafting.interpreter.jlox.ast.Parentheses;
import org.max.crafting.interpreter.jlox.ast.UnaryExpression;
import org.max.crafting.interpreter.jlox.model.Token;
import org.max.crafting.interpreter.jlox.model.TokenType;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrettyPrinterNodeVisitorTest {

    @Test
    void prettyPrinting() {

        // -6 * (2 + 3)
        Expression expression = new BinaryExpression(
                // -6
                new UnaryExpression(new Token(TokenType.MINUS, null, null, 0), new Literal(6)),

                // *
                new Token(TokenType.STAR, null, null, 0),

                // (2 + 3)
                new Parentheses(new BinaryExpression(
                        new Literal(2),
                        new Token(TokenType.PLUS, null, null, 0),
                        new Literal(3)
                ))
        );

        PrettyPrinterNodeVisitor visitor = new PrettyPrinterNodeVisitor();

        expression.accept(visitor);

        assertEquals("-6*(2+3)", visitor.getRepresentation());
    }

    @Test
    void prettyPrintingWithComparison() {

        // 1 - (2 * 3) < 4 == false
        Expression expression = new BinaryExpression(
                //1 - (2 * 3) < 4
                new BinaryExpression(
                        new Literal(1),
                        new Token(TokenType.MINUS, null, null, 0),

                        //(2 * 3) < 4
                        new BinaryExpression(
                                new Parentheses(new BinaryExpression(
                                        new Literal(2),
                                        new Token(TokenType.STAR, null, null, 0),
                                        new Literal(3))),
                                new Token(TokenType.LESS, null, null, 0),
                                new Literal(4))
                ),
                new Token(TokenType.EQUAL_EQUAL, null, null, 0),
                new Literal("false")
        );

        PrettyPrinterNodeVisitor visitor = new PrettyPrinterNodeVisitor();

        expression.accept(visitor);

        assertEquals("1-(2*3)<4==false", visitor.getRepresentation());
    }


}
