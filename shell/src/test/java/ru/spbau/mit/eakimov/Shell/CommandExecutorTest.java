package ru.spbau.mit.eakimov.Shell;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ClosedInputStream;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.spbau.mit.eakimov.Shell.CommandException;
import ru.spbau.mit.eakimov.Shell.CommandExecutor;
import ru.spbau.mit.eakimov.Shell.Environment;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.Assert.*;

public class CommandExecutorTest {
    @Rule
    public final TemporaryFolder temporaryWorkDir = new TemporaryFolder();

    @Before
    public void setWorkDir() {
        System.setProperty("user.dir", temporaryWorkDir.getRoot().getAbsolutePath());
    }

    @Test
    public void pipedCommands() throws Exception {
        final File file = temporaryWorkDir.newFile();
        final String srcFileContents = "c\na\nb";
        final String command = String.format("cat %s | sort", file.getPath());
        FileUtils.writeStringToFile(file, srcFileContents, Charset.defaultCharset());

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        new CommandExecutor(new Environment())
                .executeCommandLine(command, new ClosedInputStream(), outputStream, errorStream);
        assertEquals(Arrays.asList("a", "b", "c"),
                IOUtils.readLines(outputStream.toInputStream(), Charset.defaultCharset()));
        assertEquals(0, errorStream.toInputStream().available());
    }

    @Test(expected = CommandException.class)
    public void notFoundCommand() throws Exception {
        final String command = "wtf | exit";
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        new CommandExecutor(new Environment())
                .executeCommandLine(command, new ClosedInputStream(), outputStream, errorStream);
    }
}
