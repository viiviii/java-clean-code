package chapter14;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

abstract class MainMethodTest {
    private final PrintStream standardOut = System.out;
    private OutputStream captor;

    @BeforeEach
    protected final void init() {
        setSystemOutToMyPrintStreamForCaptor();
    }

    private void setSystemOutToMyPrintStreamForCaptor() {
        captor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captor));
    }

    @AfterEach
    protected final void printOutput() {
        setSystemOutToStandard();
        System.out.println(output());
    }

    private void setSystemOutToStandard() {
        System.setOut(standardOut);
    }

    protected final String output() {
        return captor.toString().trim();
    }

    protected abstract void runMain(String... args);
}
