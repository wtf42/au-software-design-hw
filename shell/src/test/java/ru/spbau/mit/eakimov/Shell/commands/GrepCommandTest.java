package ru.spbau.mit.eakimov.Shell.commands;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.ClosedInputStream;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.spbau.mit.eakimov.Shell.InvalidCommandException;

import java.io.File;
import java.nio.charset.Charset;

import static org.junit.Assert.*;

public class GrepCommandTest {
    @Rule
    public final TemporaryFolder temporaryWorkDir = new TemporaryFolder();

    @Test
    public void simpleString() throws Exception {
        final GrepCommand command = new GrepCommand(new String[]{"str"});
        final ByteArrayOutputStream data = new ByteArrayOutputStream();
        data.write("string\nnewline\nsubstring\n".getBytes());
        data.flush();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        command.run(data.toInputStream(), outputStream, errorStream, null);
        assertEquals(0, errorStream.size());
        assertEquals("string\nsubstring\n", outputStream.toString(Charset.defaultCharset()));
    }

    @Test
    public void fileString() throws Exception {
        final File file = temporaryWorkDir.newFile();
        final String fileContents = "string\nnewline\nsubstring\n";
        FileUtils.writeStringToFile(file, fileContents, Charset.defaultCharset());

        final GrepCommand command = new GrepCommand(new String[]{"str", file.getPath()});
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        command.run(new ClosedInputStream(), outputStream, errorStream, null);
        assertEquals(0, errorStream.size());
        assertEquals("string\nsubstring\n", outputStream.toString(Charset.defaultCharset()));
    }

    @Test
    public void regexString() throws Exception {
        final GrepCommand command = new GrepCommand(new String[]{"\\d"});
        final ByteArrayOutputStream data = new ByteArrayOutputStream();
        data.write("string\nwtf42\nsubstring\n".getBytes());
        data.flush();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        command.run(data.toInputStream(), outputStream, errorStream, null);
        assertEquals(0, errorStream.size());
        assertEquals("wtf42\n", outputStream.toString(Charset.defaultCharset()));
    }

    @Test
    public void caseInsensitiveParam() throws Exception {
        final GrepCommand command = new GrepCommand(new String[]{"-i", "Str"});
        final ByteArrayOutputStream data = new ByteArrayOutputStream();
        data.write("string\nnewline\nsUbsTring\n".getBytes());
        data.flush();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        command.run(data.toInputStream(), outputStream, errorStream, null);
        assertEquals(0, errorStream.size());
        assertEquals("string\nsUbsTring\n", outputStream.toString(Charset.defaultCharset()));
    }

    @Test
    public void wholeWordsParam() throws Exception {
        final GrepCommand command = new GrepCommand(new String[]{"-w", "str"});
        final ByteArrayOutputStream data = new ByteArrayOutputStream();
        data.write("string\nsub str ing\nsubstring\n".getBytes());
        data.flush();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        command.run(data.toInputStream(), outputStream, errorStream, null);
        assertEquals(0, errorStream.size());
        assertEquals("sub str ing\n", outputStream.toString(Charset.defaultCharset()));
    }

    @Test
    public void additionalLinesParam() throws Exception {
        final GrepCommand command = new GrepCommand(new String[]{"-A", "1", "abc"});
        final ByteArrayOutputStream data = new ByteArrayOutputStream();
        data.write("abc\ndef\nghi\n".getBytes());
        data.flush();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        command.run(data.toInputStream(), outputStream, errorStream, null);
        assertEquals(0, errorStream.size());
        assertEquals("abc\ndef\n", outputStream.toString(Charset.defaultCharset()));
    }

    @Test(expected = InvalidCommandException.class)
    public void noArgsError() throws Exception {
        final GrepCommand command = new GrepCommand(new String[]{});
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        command.run(new ClosedInputStream(), outputStream, errorStream, null);
    }

    @Test
    public void regexError() throws Exception {
        final GrepCommand command = new GrepCommand(new String[]{")))"});
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        command.run(new ClosedInputStream(), outputStream, errorStream, null);
        assertNotEquals(0, errorStream.size());
        assertEquals(0, outputStream.size());
    }

    @Test
    public void notExistingFile() throws Exception {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

        new GrepCommand(new String[] {"str", "wtf.txt"})
                .run(new ClosedInputStream(), outputStream, errorStream, null);
        assertEquals(0, outputStream.toInputStream().available());
        assertNotEquals(0, errorStream.toInputStream().available());
    }
}
