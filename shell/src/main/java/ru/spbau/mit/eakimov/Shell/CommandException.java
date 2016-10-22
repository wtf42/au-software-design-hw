package ru.spbau.mit.eakimov.Shell;

/**
 * Исключительная ситуация запуска команды
 */
public class CommandException extends ShellException {
    public CommandException(String message) {
        super(message);
    }
}
