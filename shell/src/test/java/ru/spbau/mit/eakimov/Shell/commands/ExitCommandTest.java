package ru.spbau.mit.eakimov.Shell.commands;

import org.junit.Test;
import ru.spbau.mit.eakimov.Shell.Environment;
import ru.spbau.mit.eakimov.Shell.commands.ExitCommand;

import static org.junit.Assert.*;

public class ExitCommandTest {
    @Test
    public void run() throws Exception {
        final Environment environment = new Environment();
        assertFalse(environment.isShellStopped());
        new ExitCommand(new String[] {"exit"}).run(null, null, null, environment);
        assertTrue(environment.isShellStopped());
    }
}
