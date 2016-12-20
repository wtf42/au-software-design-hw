package ru.spbau.mit.eakimov.Shell.commands;

import com.google.common.base.Splitter;
import org.apache.commons.io.IOUtils;
import ru.spbau.mit.eakimov.Shell.CommandException;
import ru.spbau.mit.eakimov.Shell.Environment;
import ru.spbau.mit.eakimov.Shell.OutputStreamUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

/**
 * Выводит на экран список файлов и папок, находящихся в текущий директории
 */
public class LsCommand extends AbstractCommandWithArguments {
    /**
     * Создание команды
     *
     * @param args аргументы команды
     */
    public LsCommand(String[] args) {
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
        String directory;
        if (args.length == 0) {
            try {
                directory = IOUtils.toString(inputStream, Charset.defaultCharset());
            } catch (IOException e) {
                OutputStreamUtils.println(errorStream, "failed to read input: " + e.getMessage());
            }
        } else if (args.length == 1) {
            directory = args[0];
        } else {
            throw new CommandException("too many arguments");
        }
        OutputStreamUtils.println(outputStream, "your output");
    }
}
