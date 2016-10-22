package ru.spbau.mit.eakimov.Shell;

/**
 * Исключительная ситуация интерпретатора
 */
public class ShellException extends Exception {
    public ShellException(String message) {
        super(message);
    }
}
