package ru.spbau.mit.eakimov.Shell.commands;

/**
 * Базовый класс команд с аргументами.
 * Сохраняет переданные в команду аргументы.
 */
public abstract class AbstractCommandWithArguments implements Command {
    protected final String[] args;

    /**
     * Создание команды
     *
     * @param args аргументы команды
     */
    public AbstractCommandWithArguments(String[] args) {
        this.args = args;
    }
}
