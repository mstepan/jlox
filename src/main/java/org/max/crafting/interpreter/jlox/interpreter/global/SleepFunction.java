package org.max.crafting.interpreter.jlox.interpreter.global;

import org.max.crafting.interpreter.jlox.interpreter.Interpreter;
import org.max.crafting.interpreter.jlox.interpreter.JLoxCallable;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Suspend current thread (sleep) for specified number of seconds.
 */
public final class SleepFunction implements JLoxCallable {

    @Override
    public Object call(Interpreter interpreter, List<Object> params) {

        Integer secondsToSleep = (Integer) params.get(0);

        try {
            TimeUnit.SECONDS.sleep(secondsToSleep);
        }
        catch (InterruptedException interEx) {
            Thread.currentThread().interrupt();
        }

        return null;
    }

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public String toString() {
        return "<native fn>";
    }
}
