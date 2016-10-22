package ru.spbau.mit.eakimov.Shell.commands;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ClosedInputStream;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Test;
import ru.spbau.mit.eakimov.Shell.CommandException;
import ru.spbau.mit.eakimov.Shell.commands.ExternalCommand;

import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ExternalCommandTest {
    @Test
    public void run() throws Exception {
        final ByteArrayOutputStream input = new ByteArrayOutputStream();
        input.write("c\na\nb".getBytes());
        input.flush();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        new ExternalCommand(new String[] {"sort"})
                .run(input.toInputStream(), outputStream, errorStream, null);
        assertEquals(Arrays.asList("a", "b", "c"),
                IOUtils.readLines(outputStream.toInputStream(), Charset.defaultCharset()));
        assertEquals(0, errorStream.toInputStream().available());
    }

    @Test(expected = CommandException.class)
    public void invalidCommand() throws Exception {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        new ExternalCommand(new String[] {"wtf"})
                .run(new ClosedInputStream(), outputStream, errorStream, null);
    }
}
