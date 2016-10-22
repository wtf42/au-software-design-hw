package ru.spbau.mit.eakimov.Shell.commands;

import org.junit.Test;
import ru.spbau.mit.eakimov.Shell.Environment;
import ru.spbau.mit.eakimov.Shell.commands.EnvironmentAssignCommand;

import static org.junit.Assert.*;

public class EnvironmentAssignCommandTest {
    @Test
    public void insertKey() throws Exception {
        final String key = "key";
        final String value = "value";
        final Environment environment = new Environment();
        assertEquals("", environment.get(key));
        new EnvironmentAssignCommand(new String[]{key, value})
                .run(null, null, null, environment);
        assertEquals(value, environment.get(key));
    }

    @Test
    public void updateKey() throws Exception {
        final String key = "key";
        final String oldValue = "oldValue";
        final String newValue = "newValue";
        final Environment environment = new Environment();
        environment.put(key, oldValue);
        assertEquals(oldValue, environment.get(key));
        new EnvironmentAssignCommand(new String[] {key, newValue})
                .run(null, null, null, environment);
        assertEquals(newValue, environment.get(key));
    }
}
