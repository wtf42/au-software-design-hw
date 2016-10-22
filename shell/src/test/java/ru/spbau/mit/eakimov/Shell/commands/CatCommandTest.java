package ru.spbau.mit.eakimov.Shell.commands;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.ClosedInputStream;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.spbau.mit.eakimov.Shell.CommandException;
import ru.spbau.mit.eakimov.Shell.commands.CatCommand;

import java.io.File;
import java.nio.charset.Charset;

import static org.junit.Assert.*;

public class CatCommandTest {
    @Rule
    public final TemporaryFolder temporaryWorkDir = new TemporaryFolder();

    @Before
    public void setWorkDir() {
        System.setProperty("user.dir", temporaryWorkDir.getRoot().getAbsolutePath());
    }

    @Test
    public void fileInput() throws Exception {
        final File file = temporaryWorkDir.newFile();
        final String fileContents = "contents\0\t42";
        FileUtils.writeStringToFile(file, fileContents, Charset.defaultCharset());

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        new CatCommand(new String[] {file.getPath()})
                .run(new ClosedInputStream(), outputStream, errorStream, null);
        assertEquals(fileContents, outputStream.toString(Charset.defaultCharset()));
        assertEquals(0, errorStream.toInputStream().available());
    }

    @Test
    public void stdInput() throws Exception {
        final String streamContents = "contents";

        final ByteArrayOutputStream input = new ByteArrayOutputStream();
        input.write(streamContents.getBytes());
        input.flush();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

        new CatCommand(new String[] {})
                .run(input.toInputStream(), outputStream, errorStream, null);
        assertEquals(streamContents, outputStream.toString(Charset.defaultCharset()));
        assertEquals(0, errorStream.toInputStream().available());
    }

    @Test
    public void notExistingFile() throws Exception {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

        new CatCommand(new String[] {"file.txt"})
                .run(new ClosedInputStream(), outputStream, errorStream, null);
        assertEquals(0, outputStream.toInputStream().available());
        assertNotEquals(0, errorStream.toInputStream().available());
    }

    @Test(expected = CommandException.class)
    public void manyArguments() throws Exception {
        new CatCommand(new String[] {"cat", "file1.txt", "file2.txt"})
                .run(null, null, null, null);
    }
}
