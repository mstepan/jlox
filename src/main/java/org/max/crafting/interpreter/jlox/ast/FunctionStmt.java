package org.max.crafting.interpreter.jlox.ast;

import org.max.crafting.interpreter.jlox.interpreter.StmtVisitor;
import org.max.crafting.interpreter.jlox.model.Token;

import java.util.List;

public class FunctionStmt implements Stmt {

    public final Token name;
    public List<Token> parameters;
    public Block body;

    public FunctionStmt(Token name, List<Token> parameters, Block body) {
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }

    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitFunction(this);
    }
}
