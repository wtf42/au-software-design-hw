package ru.spbau.mit.eakimov.Shell;

import java.util.HashMap;

/**
 * Класс хранения и управления значениями окружения
 */
public class Environment {
    private final HashMap<String, String> storage = new HashMap<>();
    private boolean stopped = false;

    /**
     * Получить значение из окружения
     * @param key имя
     * @return значение
     */
    public String get(String key) {
        return storage.getOrDefault(key, "");
    }

    /**
     * Записать значение в окружение
     * @param key   имя
     * @param value значение
     */
    public void put(String key, String value) {
        storage.put(key, value);
    }

    /**
     * Установить флаг необходимости выхода из интерпретатора
     */
    public void setShellStopped() {
        stopped = true;
    }

    /**
     * Проверка необходимости выхода из интерпретатора
     * @return true если была вызвана команда выхода, false иначе
     */
    public boolean isShellStopped() {
        return stopped;
    }
}
