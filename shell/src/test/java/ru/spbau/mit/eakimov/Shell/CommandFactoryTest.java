package ru.spbau.mit.eakimov.Shell;

import org.junit.Test;
import ru.spbau.mit.eakimov.Shell.commands.*;

import static org.junit.Assert.*;

public class CommandFactoryTest {
    @Test
    public void commandTypes() throws Exception {
        assertEquals(CatCommand.class,
                CommandFactory.createCommand(new String[] {"cat"}).getClass());
        assertEquals(EchoCommand.class,
                CommandFactory.createCommand(new String[] {"echo"}).getClass());
        assertEquals(EnvironmentAssignCommand.class,
                CommandFactory.createCommand(new String[] {"="}).getClass());
        assertEquals(ExitCommand.class,
                CommandFactory.createCommand(new String[] {"exit"}).getClass());
        assertEquals(ExternalCommand.class,
                CommandFactory.createCommand(new String[] {"sort"}).getClass());
        assertEquals(PwdCommand.class,
                CommandFactory.createCommand(new String[] {"pwd"}).getClass());
        assertEquals(WcCommand.class,
                CommandFactory.createCommand(new String[] {"wc"}).getClass());
        assertEquals(GrepCommand.class,
                CommandFactory.createCommand(new String[] {"grep", "s"}).getClass());
        assertEquals(LsCommand.class,
                CommandFactory.createCommand(new String[] {"ls"}).getClass());
        assertEquals(CdCommand.class,
                CommandFactory.createCommand(new String[] {"cd"}).getClass());
    }

    @Test(expected = InvalidCommandException.class)
    public void emptyCommand() throws Exception {
        CommandFactory.createCommand(new String[]{});
    }
}
