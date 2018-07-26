package lu.uni.serval.robotframework.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ArgumentTest {
    @Test
    public void checkSimpleMatch(){
        Argument argument = new Argument("Input password");
        String test = "Input password";

        assertTrue(argument.matches(test));
    }

    @Test
    public void checkVariableMatch(){
        Argument argument = new Argument("Login \"$(user)\" with password \"$(password)\"");
        String test = "Login \"admin\" with password \"1234\"";

        assertTrue(argument.matches(test));
    }
}
