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
import java.util.List;

import static org.junit.Assert.*;

public class LsCommandTest {
    @Rule
    public final TemporaryFolder temporaryWorkDir = new TemporaryFolder();

    @Before
    public void setWorkDir() {
        System.setProperty("user.dir", temporaryWorkDir.getRoot().getAbsolutePath());
    }

    @Test
    public void fileInput() throws Exception {
        final File file = temporaryWorkDir.newFile();
        final File directory = temporaryWorkDir.newFolder();
        final String absolutePath = temporaryWorkDir.getRoot().getAbsolutePath();

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        new LsCommand(new String[] {absolutePath})
                .run(new ClosedInputStream(), outputStream, errorStream, null);
        assertEquals(0, errorStream.toInputStream().available());

        final List<String> output = IOUtils.readLines(outputStream.toInputStream(), Charset.defaultCharset());
        assertTrue(output
                .stream()
                .anyMatch(item -> item.contains(file.getName())));
        assertTrue(output
                .stream()
                .anyMatch(item -> item.contains(directory.getName())));
    }

    @Test
    public void stdInput() throws Exception {
        final File file = temporaryWorkDir.newFile();
        final File directory = temporaryWorkDir.newFolder();
        final String absolutePath = temporaryWorkDir.getRoot().getAbsolutePath();

        final ByteArrayOutputStream input = new ByteArrayOutputStream();
        IOUtils.write(absolutePath, input, Charset.defaultCharset());

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

        new LsCommand(new String[] {})
                .run(input.toInputStream(), outputStream, errorStream, null);
        assertEquals(0, errorStream.toInputStream().available());
        final List<String> output = IOUtils.readLines(outputStream.toInputStream(), Charset.defaultCharset());
        assertTrue(output
                .stream()
                .anyMatch(item -> item.contains(file.getName())));
        assertTrue(output
                .stream()
                .anyMatch(item -> item.contains(directory.getName())));
    }

    @Test
    public void emptyInput() throws Exception {
        final File file = temporaryWorkDir.newFile();
        final File directory = temporaryWorkDir.newFolder();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

        new LsCommand(new String[] {})
                .run(new ClosedInputStream(), outputStream, errorStream, null);
        assertEquals(0, errorStream.toInputStream().available());

        final List<String> output = IOUtils.readLines(outputStream.toInputStream(), Charset.defaultCharset());
        assertTrue(output
                .stream()
                .anyMatch(item -> item.contains(file.getName())));
        assertTrue(output
                .stream()
                .anyMatch(item -> item.contains(directory.getName())));
    }

    @Test(expected = CommandException.class)
    public void notExistingFile() throws Exception {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

        new LsCommand(new String[] {"/dir1"})
                .run(new ClosedInputStream(), outputStream, errorStream, null);
    }

    @Test(expected = CommandException.class)
    public void manyArguments() throws Exception {
        new LsCommand(new String[] {"/dir1", "/dir2"})
                .run(null, null, null, null);
    }

}