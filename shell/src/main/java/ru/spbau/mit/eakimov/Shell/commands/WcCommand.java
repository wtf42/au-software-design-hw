package ru.spbau.mit.eakimov.Shell.commands;

import com.google.common.base.Splitter;
import org.apache.commons.io.IOUtils;
import ru.spbau.mit.eakimov.Shell.CommandException;
import ru.spbau.mit.eakimov.Shell.Environment;
import ru.spbau.mit.eakimov.Shell.OutputStreamUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class WcCommand extends AbstractCommandWithArguments {
    /**
     * Создание команды
     *
     * @param args аргументы команды
     */
    public WcCommand(String[] args) {
        super(args);
    }

    /**
     * Выполнить команду
     *
     * @param inputStream  поток ввода команды
     * @param outputStream поток вывода команды
     * @param errorStream  поток вывода ошибок выполнения команды
     * @param environment  окружение интерпретатора
     * @throws CommandException в случае ошибки запуска команды
     */
    @Override
    public void run(InputStream inputStream,
                    OutputStream outputStream,
                    OutputStream errorStream,
                    Environment environment) throws CommandException {
        InputStream sourceStream;
        if (args.length == 0) {
            sourceStream = inputStream;
        } else if (args.length == 1) {
            try {
                sourceStream = new FileInputStream(args[0]);
            } catch (FileNotFoundException e) {
                OutputStreamUtils.println(errorStream, "argument file not found");
                return;
            }
        } else {
            throw new CommandException("too many arguments");
        }

        try {
            final String contents = IOUtils.toString(sourceStream, Charset.defaultCharset());
            int lines = contents.isEmpty() ? 0 : Splitter.on('\n').splitToList(contents).size();
            int words = Splitter.on(Pattern.compile("\\s+"))
                    .trimResults().omitEmptyStrings().splitToList(contents).size();
            int bytes = contents.length();
            OutputStreamUtils.println(outputStream, String.format("%d %d %d", lines, words, bytes));
        } catch (IOException e) {
            OutputStreamUtils.println(errorStream, "failed to read input: " + e.getMessage());
        }
    }
}
