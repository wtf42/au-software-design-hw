package ru.spbau.mit.eakimov.Shell.commands;

import ru.spbau.mit.eakimov.Shell.CommandException;
import ru.spbau.mit.eakimov.Shell.Environment;
import ru.spbau.mit.eakimov.Shell.OutputStreamUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Команда, печатающая в поток вывода аргументы, с которыми ее вызвали
 */
public class EchoCommand extends AbstractCommandWithArguments {
    /**
     * Создание команды
     *
     * @param args аргументы команды
     */
    public EchoCommand(String[] args) {
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
        final String result = Stream.of(args).collect(Collectors.joining(" "));
        OutputStreamUtils.println(outputStream, result);
    }
}
