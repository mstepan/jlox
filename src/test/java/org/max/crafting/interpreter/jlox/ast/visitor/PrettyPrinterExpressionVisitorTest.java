package org.max.crafting.interpreter.jlox.ast.visitor;

import org.junit.jupiter.api.Test;
import org.max.crafting.interpreter.jlox.ast.BinaryExpr;
import org.max.crafting.interpreter.jlox.ast.Expression;
import org.max.crafting.interpreter.jlox.ast.Literal;
import org.max.crafting.interpreter.jlox.ast.Grouping;
import org.max.crafting.interpreter.jlox.ast.UnaryExpr;
import org.max.crafting.interpreter.jlox.interpreter.PrettyPrinterExpressionVisitor;
import org.max.crafting.interpreter.jlox.model.Token;
import org.max.crafting.interpreter.jlox.model.TokenType;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class PrettyPrinterExpressionVisitorTest {

    @Test
    void prettyPrinting() {

        // -6 * (2 + 3)
        Expression expression = new BinaryExpr(
                // -6
                new UnaryExpr(new Token(TokenType.MINUS, null, null, 0), new Literal(6)),

                // *
                new Token(TokenType.STAR, null, null, 0),

                // (2 + 3)
                new Grouping(new BinaryExpr(
                        new Literal(2),
                        new Token(TokenType.PLUS, null, null, 0),
                        new Literal(3)
                ))
        );

        PrettyPrinterExpressionVisitor visitor = new PrettyPrinterExpressionVisitor();

        String repr = (String)expression.accept(visitor);

        assertEquals("-6 * (2 + 3)", repr);
    }

    @Test
    void prettyPrintingWithComparison() {

        // 1 - (2 * 3) < 4 == false
        Expression expression = new BinaryExpr(
                //1 - (2 * 3) < 4
                new BinaryExpr(
                        new Literal(1),
                        new Token(TokenType.MINUS, null, null, 0),

                        //(2 * 3) < 4
                        new BinaryExpr(
                                new Grouping(new BinaryExpr(
                                        new Literal(2),
                                        new Token(TokenType.STAR, null, null, 0),
                                        new Literal(3))),
                                new Token(TokenType.LESS, null, null, 0),
                                new Literal(4))
                ),
                new Token(TokenType.EQUAL_EQUAL, null, null, 0),
                new Literal("false")
        );

        PrettyPrinterExpressionVisitor visitor = new PrettyPrinterExpressionVisitor();

        String repr = (String)expression.accept(visitor);

        assertEquals("1 - (2 * 3) < 4 == false", repr);
    }


}
