package org.ukwikora.model;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.List;

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

    @Test
    public void checkScalarVariableMatch(){
        String raw = "Login \"@{user}\" with password \"${password}\" with options \"&{options}\"";
        List<String> variables = Value.findVariables(raw);

        assertEquals(3, variables.size());
        assertEquals("@{user}", variables.get(0));
        assertEquals("${password}", variables.get(1));
        assertEquals("&{options}", variables.get(2));
    }

    @Test
    public void checkIsVariable() {
        String scalar = "${ScalarVariable}";
        assertTrue(Value.isVariable(scalar));

        String list = "@{ListVariable}";
        assertTrue(Value.isVariable(list));

        String dict = "&{DictVariable}";
        assertTrue(Value.isVariable(dict));

        String regularString = "String";
        assertFalse(Value.isVariable(regularString));
    }

    @Test
    public void checkHasVariable() {
        String scalar = "Some text \"${ScalarVariable}\" More text";
        assertTrue(Value.hasVariable(scalar));

        String list = "Some text \"@{ListVariable}\" More text";
        assertTrue(Value.hasVariable(list));

        String dict = "Some text \"&{DictVariable}\" More text";
        assertTrue(Value.hasVariable(dict));

        String regularString = "String with some text";
        assertFalse(Value.hasVariable(regularString));
    }
}
