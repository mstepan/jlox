package org.max.crafting.interpreter.jlox;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;

public abstract class LoxBaseTest {

    private static final ByteArrayOutputStream OUT = new ByteArrayOutputStream();
    private static final ByteArrayOutputStream ERR = new ByteArrayOutputStream();

    public static String output() {
        return new String(OUT.toByteArray(), Charset.defaultCharset());
    }

    public static String errorOutput() {
        return new String(ERR.toByteArray(), Charset.defaultCharset());
    }

    @BeforeAll
    public static void setUp() {
        System.setOut(new PrintStream(OUT));
        System.setErr(new PrintStream(ERR));
    }

    @AfterEach
    public void tearDown() {
        OUT.reset();
        ERR.reset();
    }
}
