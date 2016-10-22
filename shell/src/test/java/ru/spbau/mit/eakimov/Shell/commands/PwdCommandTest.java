package ru.spbau.mit.eakimov.Shell.commands;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ClosedInputStream;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.spbau.mit.eakimov.Shell.commands.PwdCommand;

import java.nio.charset.Charset;
import java.util.Collections;

import static org.junit.Assert.*;

public class PwdCommandTest {
    @Rule
    public final TemporaryFolder temporaryWorkDir = new TemporaryFolder();

    @Before
    public void setWorkDir() {
        System.setProperty("user.dir", temporaryWorkDir.getRoot().getAbsolutePath());
    }

    @Test
    public void run() throws Exception {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        new PwdCommand(new String[]{"pwd"})
                .run(new ClosedInputStream(), outputStream, errorStream, null);
        assertEquals(Collections.singletonList(temporaryWorkDir.getRoot().getAbsolutePath()),
                IOUtils.readLines(outputStream.toInputStream(), Charset.defaultCharset()));
        assertEquals(0, errorStream.toInputStream().available());
    }
}
