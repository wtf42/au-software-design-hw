package ru.spbau.mit.eakimov.Shell.commands;

import org.apache.commons.io.IOUtils;
import ru.spbau.mit.eakimov.Shell.CommandException;
import ru.spbau.mit.eakimov.Shell.Environment;
import ru.spbau.mit.eakimov.Shell.OutputStreamUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Выводит на экран список файлов и папок, находящихся в текущий директории
 */
public class LsCommand extends AbstractCommandWithArguments {
    private final static String DIRECTORY_PROPERTY = "user.dir";

    public LsCommand(String[] args) {
        super(args);
    }

    @Override
    public void run(InputStream inputStream,
                    OutputStream outputStream,
                    OutputStream errorStream,
                    Environment environment) throws CommandException {
        String directoryName = System.getProperty(DIRECTORY_PROPERTY);
        if (args.length == 0) {
            try {
                final String targetDir = IOUtils.toString(inputStream, Charset.defaultCharset());
                if (!targetDir.isEmpty()) {
                    directoryName = targetDir;
                }
            } catch (IOException e) {
                OutputStreamUtils.println(errorStream, "failed to read input: " + e.getMessage());
            }
        } else if (args.length == 1) {
            directoryName = args[0];
        } else {
            throw new CommandException("too many arguments");
        }
        // Validates directory
        final File directory = new File(directoryName);
        if (!directory.exists()) {
            throw new CommandException("invalid directory : " + directoryName);
        }
        // Handles valid input
        if (directory.isDirectory()) {
            // ls dir
            final StringBuilder sb = new StringBuilder();
            Arrays.stream(directory.list())
                    .forEach(item -> sb.append(item).append("\n"));
            OutputStreamUtils.println(outputStream, sb.toString());
        } else {
            // ls file
            OutputStreamUtils.println(outputStream, directoryName);
        }
    }
}
