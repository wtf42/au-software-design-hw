package ru.spbau.mit.eakimov.Shell.commands;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ClosedInputStream;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.spbau.mit.eakimov.Shell.CommandException;

import java.io.File;
import java.nio.charset.Charset;

import static org.junit.Assert.*;

public class CdCommandTest {
    @Rule
    public final TemporaryFolder temporaryWorkDir = new TemporaryFolder();

    @Before
    public void setWorkDir() {
        System.setProperty("user.dir", temporaryWorkDir.getRoot().getAbsolutePath());
    }

    @Test
    public void fileInput() throws Exception {
        final File directory = temporaryWorkDir.newFolder();
        final String absolutePath = directory.getAbsolutePath();

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        new CdCommand(new String[] {directory.getPath()})
                .run(new ClosedInputStream(), outputStream, errorStream, null);
        assertEquals(0, errorStream.toInputStream().available());

        final String currentDir = System.getProperty("user.dir");
        assertEquals(absolutePath, currentDir);
    }

    @Test
    public void stdInput() throws Exception {
        final File directory = temporaryWorkDir.newFolder();
        final String absolutePath = directory.getAbsolutePath();
        final ByteArrayOutputStream input = new ByteArrayOutputStream();
        IOUtils.write(absolutePath, input, Charset.defaultCharset());

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

        new CdCommand(new String[] {})
                .run(input.toInputStream(), outputStream, errorStream, null);
        assertEquals(0, errorStream.toInputStream().available());

        final String currentDir = System.getProperty("user.dir");
        assertEquals(absolutePath, currentDir);
    }

    @Test(expected = CommandException.class)
    public void emptyInput() throws Exception {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

        new CdCommand(new String[] {})
                .run(new ClosedInputStream(), outputStream, errorStream, null);
        assertEquals(0, errorStream.toInputStream().available());
    }

    @Test(expected = CommandException.class)
    public void notExistingFile() throws Exception {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

        new CdCommand(new String[] {"file.txt"})
                .run(new ClosedInputStream(), outputStream, errorStream, null);
    }

    @Test(expected = CommandException.class)
    public void manyArguments() throws Exception {
        new CdCommand(new String[] {"file1.txt", "file2.txt"})
                .run(null, null, null, null);
    }
}