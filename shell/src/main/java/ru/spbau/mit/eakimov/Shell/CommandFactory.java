package ru.spbau.mit.eakimov.Shell;

import com.google.common.collect.ImmutableMap;
import ru.spbau.mit.eakimov.Shell.commands.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Создание экземпляра команды в зависимости от аргументов командной строки
 */
public class CommandFactory {
    private static final Map<String, Class<? extends Command>> commands = ImmutableMap
            .<String, Class<? extends Command>>builder()
            .put("echo", EchoCommand.class)
            .put("cat", CatCommand.class)
            .put("wc", WcCommand.class)
            .put("pwd", PwdCommand.class)
            .put("exit", ExitCommand.class)
            .put("=", EnvironmentAssignCommand.class)
            .build();

    /**
     * Создание экземпляра команды, выбирается из перечисленного выше набора базовых команд
     * в случае отсутствия команды в наборе - создается экземпляр для запуска внешней команды
     *
     * @param args аргументы командной строки
     * @return созданный экземпляр команды, готовый к выполнению
     * @throws ShellException в случае отсутствия команды в аргументе или ошибки создания экземпляра команды
     */
    public static Command createCommand(String[] args) throws ShellException {
        if (args.length == 0) {
            throw new InvalidCommandException("cannot create empty command");
        }
        try {
            if (!commands.containsKey(args[0])) {
                return new ExternalCommand(args);
            } else {
                final String[] cmdArgs = Arrays.stream(args).skip(1)
                        .collect(Collectors.toList()).toArray(new String[0]);
                return commands.get(args[0])
                        .getConstructor(String[].class)
                        .newInstance((Object) cmdArgs);
            }
        } catch (InstantiationException
                | IllegalAccessException
                | NoSuchMethodException
                | InvocationTargetException e) {
            throw new ShellException("failed to create command: " + e.getMessage());
        }
    }
}
