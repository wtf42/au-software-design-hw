package ru.spbau.mit.eakimov.Shell.commands;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ClosedInputStream;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Test;
import ru.spbau.mit.eakimov.Shell.commands.EchoCommand;

import java.nio.charset.Charset;
import java.util.Collections;

import static org.junit.Assert.*;

public class EchoCommandTest {
    @Test
    public void emptyInput() throws Exception {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        new EchoCommand(new String[] {})
                .run(new ClosedInputStream(), outputStream, errorStream, null);
        assertEquals(Collections.singletonList(""),
                IOUtils.readLines(outputStream.toInputStream(), Charset.defaultCharset()));
        assertEquals(0, errorStream.toInputStream().available());
    }

    @Test
    public void simpleArguments() throws Exception {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        new EchoCommand(new String[] {"abc", "def", "qwerty"})
                .run(new ClosedInputStream(), outputStream, errorStream, null);
        assertEquals(Collections.singletonList("abc def qwerty"),
                IOUtils.readLines(outputStream.toInputStream(), Charset.defaultCharset()));
        assertEquals(0, errorStream.toInputStream().available());
    }
}
