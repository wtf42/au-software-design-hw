package ru.spbau.mit.eakimov.Shell;

import org.junit.Test;
import ru.spbau.mit.eakimov.Shell.Environment;
import ru.spbau.mit.eakimov.Shell.Parser;

import java.util.Arrays;

import static org.junit.Assert.*;

public class ParserTest {
    @Test
    public void validates() throws Exception {
        assertTrue(Parser.validateCommandLine("cat 'f \"1' | sort | wc"));
        assertTrue(Parser.validateCommandLine("cat \"$x 'a'\" | 'wc'"));
        assertFalse(Parser.validateCommandLine("cat ' \"a ' \" | wc"));
        assertFalse(Parser.validateCommandLine("cat \" 'a' \" ' | wc"));
    }

    @Test
    public void splitToPipes() throws Exception {
        assertEquals(Arrays.asList("echo 'a | b' ", " wc"),
                Parser.splitCommandLineToPipes("echo 'a | b' | wc"));
        assertEquals(Arrays.asList("echo \"a | b\" ", " wc"),
                Parser.splitCommandLineToPipes("echo \"a | b\" | wc"));
        assertEquals(Arrays.asList("abc ", " def ", " q      ", " w e r t y"),
                Parser.splitCommandLineToPipes("abc | def | q      | w e r t y"));
    }

    @Test
    public void substituteEnvironment() throws Exception {
        final Environment environment = new Environment();
        environment.put("x", "x_val");
        environment.put("y", "y_val");
        environment.put("iddqd", "42");
        assertEquals("echo '$x vs y'",
                Parser.substituteEnvironment("echo '$x vs y'", environment));
        assertEquals("echo \"x_val vs y\" | sort y_val",
                Parser.substituteEnvironment("echo \"$x vs y\" | sort $y", environment));
        assertEquals("echo \"x_valy_val=42\"",
                Parser.substituteEnvironment("echo \"$x$y=$iddqd\"", environment));
        assertEquals("echo \"'x_val' vs y\"",
                Parser.substituteEnvironment("echo \"'$x' vs y\"", environment));
    }

    @Test
    public void splitArguments() throws Exception {
        assertEquals(Arrays.asList("echo", "a or b", "z", "x 'or y"),
                Parser.parseCommandArguments("echo 'a or b' z \"x 'or y\" "));
        assertEquals(Arrays.asList("=", "qwe", "rty zxc"),
                Parser.parseCommandArguments("qwe=rty zxc"));
    }
}
