package ru.spbau.mit.eakimov.Shell;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import ru.spbau.mit.eakimov.Shell.commands.Command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс, хранящий окружение интерпретатора и позволяющий исполнить строку с командой
 */
public class CommandExecutor {
    private final Environment environment;

    public CommandExecutor(Environment environment) {
        this.environment = environment;
    }

    /**
     * Необходимость завершения работы интерпретатора, указанная пользователем
     * @return true если необходимо завершить работу интерпретатора и false иначе
     */
    public boolean shouldStop() {
        return environment.isShellStopped();
    }

    /**
     * Выполняет строку с командой
     * @param line         строка команды
     * @param inputStream  поток ввода
     * @param outputStream поток вывода результатов
     * @param errorStream  поток вывода ошибок выполнения команды
     * @throws ShellException в случае ошибок выполнения команды
     */
    public void executeCommandLine(String line,
                                   InputStream inputStream,
                                   OutputStream outputStream,
                                   OutputStream errorStream) throws ShellException {
        if (!Parser.validateCommandLine(line)) {
            throw new InvalidCommandException("failed to match quotes");
        }
        final List<String> pipedCommands = Parser.splitCommandLineToPipes(line)
                .stream().map(cmd -> Parser.substituteEnvironment(cmd, environment))
                .collect(Collectors.toList());
        InputStream currentInput = inputStream;
        ByteArrayOutputStream temporaryOutput;
        for (String singleCommand : pipedCommands) {
            final String[] args = Parser.parseCommandArguments(singleCommand).toArray(new String[0]);
            final Command command = CommandFactory.createCommand(args);
            temporaryOutput = new ByteArrayOutputStream();
            command.run(currentInput, temporaryOutput, errorStream, environment);
            currentInput = temporaryOutput.toInputStream();
        }
        try {
            IOUtils.copy(currentInput, outputStream);
        } catch (IOException e) {
            throw new ShellException("failed to copy command output");
        }
    }
}
