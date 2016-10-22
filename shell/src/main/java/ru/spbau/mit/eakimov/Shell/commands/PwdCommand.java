package ru.spbau.mit.eakimov.Shell.commands;

import ru.spbau.mit.eakimov.Shell.CommandException;
import ru.spbau.mit.eakimov.Shell.Environment;
import ru.spbau.mit.eakimov.Shell.OutputStreamUtils;

import java.io.InputStream;
import java.io.OutputStream;

public class PwdCommand extends AbstractCommandWithArguments {
    /**
     * Создание команды
     *
     * @param args аргументы команды
     */
    public PwdCommand(String[] args) {
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
        OutputStreamUtils.println(outputStream, System.getProperty("user.dir"));
    }
}
