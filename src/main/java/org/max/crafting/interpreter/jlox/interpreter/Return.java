package org.max.crafting.interpreter.jlox.interpreter;

/**
 * This class will be used as a control-flow RETURN statement equivalent.
 */
final class Return extends RuntimeException {

    final Object value;

    public Return(Object value) {
        // call below constructor, to do not fill-in stack traces
        super(null, null, false, false);
        this.value = value;
    }

}
