package ru.spbau.mit.eakimov.Shell.commands;

import org.apache.commons.io.IOUtils;
import ru.spbau.mit.eakimov.Shell.CommandException;
import ru.spbau.mit.eakimov.Shell.Environment;
import ru.spbau.mit.eakimov.Shell.OutputStreamUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Перемещается из текущей директории в другую директорию
 */
public class CdCommand extends AbstractCommandWithArguments {
    private final static String DIRECTORY_PROPERTY = "user.dir";

    public CdCommand(String[] args) {
        super(args);
    }

    @Override
    public void run(InputStream inputStream,
                    OutputStream outputStream,
                    OutputStream errorStream,
                    Environment environment) throws CommandException {
        String directoryName = "";
        // Handling arguments
        if (args.length == 0) {
            try {
                directoryName = IOUtils.toString(inputStream, Charset.defaultCharset());
            } catch (IOException e) {
                OutputStreamUtils.println(errorStream, "failed to read input: " + e.getMessage());
            }
        } else if (args.length == 1) {
            directoryName = args[0];
        } else {
            throw new CommandException("too many arguments for CdCommand");
        }
        // cd logic
        final File directory = new File(directoryName);
        if (directory.exists() && directory.isDirectory()) {
            final String absolutePath = directory.getAbsolutePath();
            System.setProperty(DIRECTORY_PROPERTY, absolutePath);
        } else {
            throw  new CommandException("directory is wrong : " + directoryName);
        }
    }
}
