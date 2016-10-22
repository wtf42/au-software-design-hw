package ru.spbau.mit.eakimov.Shell;

import com.google.common.base.Splitter;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс для обработки строки с набором команд, записанных через пайп
 */
public class Parser {
    private static final String hardQuotesGroup = "'([^']*)'";
    private static final String softQuotesGroup = "\"([^\"]*)\"";

    private static final String nonSoftHardQuotesChar = "[^\"']";
    private static final String suffixRegex = String.format("(%s*(%s|%s))*%s*$",
            nonSoftHardQuotesChar, softQuotesGroup, hardQuotesGroup, nonSoftHardQuotesChar);
    private static final String suffixCheckRegex = String.format("(?=%s)", suffixRegex);

    private static final String commandRegex = String.format("^%s", suffixRegex);
    private static final Pattern commandRegexPattern = Pattern.compile(commandRegex);

    private static final String pipeRegex = String.format("\\|%s", suffixCheckRegex);
    private static final Pattern pipeRegexPattern = Pattern.compile(pipeRegex);

    private static final String envRegex = String.format("\\$([a-z]+)(?=([^\"]*\")?%s)", suffixRegex);
    private static final Pattern envRegexPattern = Pattern.compile(envRegex);

    private static final String nonWhitespaceQuotesChar = "[^ \t'\"]";
    private static final String argumentsRegex = String.format("((%s+)|%s|%s)%s",
            nonWhitespaceQuotesChar, hardQuotesGroup, softQuotesGroup, suffixCheckRegex);
    private static final Pattern argumentsRegexPattern = Pattern.compile(argumentsRegex);

    private static final Pattern assignRegexPattern = Pattern.compile("^([a-z]+)=(.*)$");

    /**
     * Проверяет корректность количества открытых и закрытых кавычек в тексте команды
     *
     * @param commandLine текст команды
     * @return true в случае корректности набора команд и false иначе
     */
    public static boolean validateCommandLine(String commandLine) {
        return commandRegexPattern.matcher(commandLine).matches();
    }

    /**
     * Разделение исходной строки на части по символу пайпа
     * с поддержкой одинарных и двойных кавычек
     *
     * @param line исходная строка, введенная пользователем
     * @return список строк, которые находятся между символами пайпа
     */
    public static List<String> splitCommandLineToPipes(String line) {
        return Splitter.on(pipeRegexPattern).splitToList(line);
    }

    /**
     * Подстановка значений из окружения в текст команды
     * значения окружения начинаются с $ и содержат только буквы [a-z]
     * не подставляются в одинарные кавычки
     *
     * @param command текст команды
     * @param env     переменные окружения
     * @return текст команды с подставленными значениями переменных окружения
     */
    public static String substituteEnvironment(String command, Environment env) {
        final Matcher matcher = envRegexPattern.matcher(command);
        final List<MatchResult> results = new ArrayList<>();
        while (matcher.find()) {
            results.add(matcher.toMatchResult());
        }
        Collections.reverse(results);

        final StringBuilder resultCommand = new StringBuilder(command);
        for (MatchResult matchResult : results) {
            final String variable = matchResult.group(1);
            final String value = env.get(variable);
            resultCommand.replace(matchResult.start(1) - 1, matchResult.end(1), value);
        }
        return resultCommand.toString();
    }

    /**
     * Разделение одной команды на аргументы с поддержкой кавычек
     * с отдельным случаем для операции присваивания переменной окружения
     *
     * @param command текст команды
     * @return команда и ее аргументы
     */
    public static List<String> parseCommandArguments(String command) {
        final Matcher assignMatcher = assignRegexPattern.matcher(command);
        if (assignMatcher.matches()) {
            final MatchResult matchResult = assignMatcher.toMatchResult();
            return Arrays.asList("=", matchResult.group(1), matchResult.group(2));
        }
        final List<String> result = new ArrayList<>();
        final Matcher matcher = argumentsRegexPattern.matcher(command);
        while (matcher.find()) {
            final MatchResult matchResult = matcher.toMatchResult();
            for (int groupIdx = 2; groupIdx <= 4; groupIdx++) {
                if (matchResult.group(groupIdx) != null) {
                    result.add(matchResult.group(groupIdx));
                }
            }
        }
        return result;
    }
}
