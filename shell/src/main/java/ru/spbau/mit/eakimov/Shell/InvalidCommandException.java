package ru.spbau.mit.eakimov.Shell;

/**
 * Исключение неверного ввода команды
 */
public class InvalidCommandException extends ShellException {
    public InvalidCommandException(String message) {
        super(message);
    }
}
