package ru.spbau.mit.eakimov.Shell;

import java.io.*;

/**
 * Точка входа в программу, считывает строки с командами и запускает их выполнение
 */
public class SimpleShell {
    public static void main(String[] args) {
        final CommandExecutor executor = new CommandExecutor(new Environment());
        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (!executor.shouldStop()) {
            try {
                final String line = reader.readLine();
                if (line == null) {
                    break;
                }
                executor.executeCommandLine(line, System.in, System.out, System.err);
            } catch (IOException e) {
                OutputStreamUtils.println(System.err, "failed to read input command: " + e.getMessage());
            } catch (ShellException e) {
                OutputStreamUtils.println(System.err, e.getMessage());
            }
        }
    }
}
