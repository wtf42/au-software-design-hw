package ru.spbau.mit.eakimov.Shell.commands;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ClosedInputStream;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.spbau.mit.eakimov.Shell.CommandException;
import ru.spbau.mit.eakimov.Shell.commands.WcCommand;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class WcCommandTest {
    @Rule
    public final TemporaryFolder temporaryWorkDir = new TemporaryFolder();

    @Before
    public void setWorkDir() {
        System.setProperty("user.dir", temporaryWorkDir.getRoot().getAbsolutePath());
    }

    @Test
    public void fileInput() throws Exception {
        final File file = temporaryWorkDir.newFile();
        final List<String> fileContents = Arrays.asList("contents\t42", "line 2");
        FileUtils.writeLines(file, fileContents);
        final long fileSize = FileUtils.sizeOf(file);

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        new WcCommand(new String[] {file.getPath()})
                .run(new ClosedInputStream(), outputStream, errorStream, null);
        assertEquals(Collections.singletonList(
                String.format("3 4 %d", fileSize)),
                IOUtils.readLines(outputStream.toInputStream(), Charset.defaultCharset()));
        assertEquals(0, errorStream.toInputStream().available());
    }

    @Test
    public void stdInput() throws Exception {
        final String streamContents = "contents\t42\nline 2\n";
        final ByteArrayOutputStream input = new ByteArrayOutputStream();
        IOUtils.write(streamContents, input, Charset.defaultCharset());

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

        new WcCommand(new String[] {})
                .run(input.toInputStream(), outputStream, errorStream, null);
        assertEquals(Collections.singletonList(
                String.format("3 4 %d", streamContents.length())),
                IOUtils.readLines(outputStream.toInputStream(), Charset.defaultCharset()));
        assertEquals(0, errorStream.toInputStream().available());
    }

    @Test
    public void emptyInput() throws Exception {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

        new WcCommand(new String[] {})
                .run(new ClosedInputStream(), outputStream, errorStream, null);
        assertEquals(Collections.singletonList("0 0 0"),
                IOUtils.readLines(outputStream.toInputStream(), Charset.defaultCharset()));
        assertEquals(0, errorStream.toInputStream().available());
    }

    @Test
    public void emptyLines() throws Exception {
        final String streamContents = "\n\n\n";
        final ByteArrayOutputStream input = new ByteArrayOutputStream();
        IOUtils.write(streamContents, input, Charset.defaultCharset());

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

        new WcCommand(new String[] {})
                .run(input.toInputStream(), outputStream, errorStream, null);
        assertEquals(Collections.singletonList(
                String.format("4 0 %d", streamContents.length())),
                IOUtils.readLines(outputStream.toInputStream(), Charset.defaultCharset()));
        assertEquals(0, errorStream.toInputStream().available());
    }

    @Test
    public void notExistingFile() throws Exception {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

        new WcCommand(new String[] {"file.txt"})
                .run(new ClosedInputStream(), outputStream, errorStream, null);
        assertEquals(0, outputStream.toInputStream().available());
        assertNotEquals(0, errorStream.toInputStream().available());
    }

    @Test(expected = CommandException.class)
    public void manyArguments() throws Exception {
        new WcCommand(new String[] {"file1.txt", "file2.txt"})
                .run(null, null, null, null);
    }
}
