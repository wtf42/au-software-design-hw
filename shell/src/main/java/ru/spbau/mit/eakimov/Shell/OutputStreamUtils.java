package ru.spbau.mit.eakimov.Shell;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Инструменты ввода-вывода
 */
public class OutputStreamUtils {
    /**
     * Запись строки в поток с игнорированием ошибок записи
     *
     * @param outputStream поток для записи
     * @param message      строка
     */
    public static void println(OutputStream outputStream, String message) {
        try {
            outputStream.write(message.getBytes());
            outputStream.write("\n".getBytes());
            outputStream.flush();
        } catch (IOException ignored) {}
    }
}
