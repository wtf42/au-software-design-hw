package ru.spbau.mit.eakimov.Shell.commands;

import org.apache.commons.io.IOUtils;
import ru.spbau.mit.eakimov.Shell.CommandException;
import ru.spbau.mit.eakimov.Shell.Environment;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Запуск внешней команды
 */
public class ExternalCommand extends AbstractCommandWithArguments {
    /**
     * Создание команды
     *
     * @param args аргументы команды
     */
    public ExternalCommand(String[] args) {
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
            final ProcessBuilder processBuilder = new ProcessBuilder(args);
            final Process process = processBuilder.start();

            IOUtils.copy(inputStream, process.getOutputStream());
            process.getOutputStream().close();
            process.waitFor();
            IOUtils.copy(process.getInputStream(), outputStream);
            IOUtils.copy(process.getErrorStream(), errorStream);
            outputStream.flush();
            errorStream.flush();
        } catch (Exception e) {
            throw new CommandException("failed to execute external command: " + e.getMessage());
        }
    }
}
