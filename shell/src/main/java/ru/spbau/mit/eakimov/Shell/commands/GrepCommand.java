package ru.spbau.mit.eakimov.Shell.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import ru.spbau.mit.eakimov.Shell.CommandException;
import ru.spbau.mit.eakimov.Shell.Environment;
import ru.spbau.mit.eakimov.Shell.InvalidCommandException;
import ru.spbau.mit.eakimov.Shell.OutputStreamUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Команда grep,
 * поддерживающая ключи
 *   -i (нечувствительность к регистру)
 *   -w (поиск только слов целиком)
 *   -A n (распечатать n строк после строки с совпадением)
 * поддерживающая регулярные выражения в строке поиска,
 * использующая одну из библиотек для разбора аргументов командной строки.
 */
public class GrepCommand implements Command {
    @Parameter(names = "-i", description = "case insensitive")
    private boolean caseInsensitive = false;

    @Parameter(names = "-w", description = "whole words only")
    private boolean wholeWords = false;

    @Parameter(names = "-A", description = "additional lines after match")
    private int additionalLines = 0;

    @Parameter(description = "regex [file]", required = true)
    private List<String> parameters;

    /**
     * Создание команды
     *
     * @param args аргументы команды
     */
    public GrepCommand(String[] args) throws InvalidCommandException {
        try {
            new JCommander(this, args);
        } catch (ParameterException e) {
            throw new InvalidCommandException("failed to parse grep arguments: " + e.getMessage());
        }
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
        String regex;
        final String fileName;
        if (parameters.size() == 1) {
            regex = parameters.get(0);
            fileName = null;
        } else if (parameters.size() == 2) {
            regex = parameters.get(0);
            fileName = parameters.get(1);
        } else {
            throw new CommandException("too many arguments");
        }

        if (wholeWords) {
            regex = "(?<=(^|\\W))" + regex + "(?=(\\W|$))";
        }

        final Pattern pattern;
        try {
            pattern = Pattern.compile(regex, caseInsensitive ? Pattern.CASE_INSENSITIVE : 0);
        } catch (PatternSyntaxException e) {
            OutputStreamUtils.println(errorStream, "failed to parse regex: " + e.getMessage());
            return;
        }

        try {
            final InputStream sourceStream = fileName == null
                    ? inputStream
                    : new FileInputStream(fileName);
            final List<String> lines = IOUtils.readLines(sourceStream, Charset.defaultCharset());
            for (int i = 0; i < lines.size(); i++) {
                if (pattern.matcher(lines.get(i)).find()) {
                    for (int j = 0; j < 1 + additionalLines && i + j < lines.size(); j++) {
                        outputStream.write(lines.get(i + j).getBytes());
                        outputStream.write("\n".getBytes());
                        outputStream.flush();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            OutputStreamUtils.println(errorStream, "file not found");
        } catch (IOException e) {
            OutputStreamUtils.println(errorStream, "failed to read file contents: " + e.getMessage());
        }
    }
}
