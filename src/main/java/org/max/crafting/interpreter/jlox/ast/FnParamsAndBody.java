package org.max.crafting.interpreter.jlox.ast;

import org.max.crafting.interpreter.jlox.model.Token;

import java.util.List;

public class FnParamsAndBody {

    public final List<Token> parameters;
    public final Block body;

    public FnParamsAndBody(List<Token> parameters, Block body) {
        this.parameters = parameters;
        this.body = body;
    }
}
