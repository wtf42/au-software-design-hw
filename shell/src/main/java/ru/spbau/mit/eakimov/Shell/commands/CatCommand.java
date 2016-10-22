package ru.spbau.mit.eakimov.Shell.commands;

import org.apache.commons.io.IOUtils;
import ru.spbau.mit.eakimov.Shell.CommandException;
import ru.spbau.mit.eakimov.Shell.Environment;
import ru.spbau.mit.eakimov.Shell.OutputStreamUtils;

import java.io.*;

/**
 * Печать в поток вывода содержимое файлов, имена которых переданы в аргументах команды
 */
public class CatCommand extends AbstractCommandWithArguments {
    /**
     * Создание команды
     *
     * @param args аргументы команды
     */
    public CatCommand(String[] args) {
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
        try {
            InputStream sourceStream;
            if (args.length == 0) {
                sourceStream = inputStream;
            } else if (args.length == 1) {
                sourceStream = new FileInputStream(args[0]);
            } else {
                throw new CommandException("too many arguments");
            }
            IOUtils.copy(sourceStream, outputStream);
            outputStream.flush();
        } catch (FileNotFoundException e) {
            OutputStreamUtils.println(errorStream, "file not found");
        } catch (IOException e) {
            OutputStreamUtils.println(errorStream, "failed to read file contents: " + e.getMessage());
        }
    }
}
