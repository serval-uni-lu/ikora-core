package org.ukwikora.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ValueTest {
    @Test
    public void checkSimpleMatch(){
        Value value = new Value("Input password");
        String test = "Input password";

        assertTrue(value.matches(test));
    }

    @Test
    public void checkVariableMatch(){
        Value value = new Value("Login \"${user}\" with password \"${password}\"");
        String test = "Login \"admin\" with password \"1234\"";

        assertTrue(value.matches(test));
    }
}
