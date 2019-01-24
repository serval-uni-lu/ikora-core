package org.ukwikora.model;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.List;
import java.util.Optional;

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
    public void checkFindVariables(){
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

    @Test
    public void checkResolvedSimpleValues(){
        ScalarVariable name = new ScalarVariable();
        name.addElement("John Smith");

        ScalarVariable password = new ScalarVariable();
        password.addElement("secret");

        Value value = new Value("Login with ${name} and ${password}");
        value.setVariable("${name}", name);
        value.setVariable("${password}", password);

        Optional<List<Value>> resolvedValues = value.getResolvedValues();

        assertTrue(resolvedValues.isPresent());
        assertEquals(1, resolvedValues.get().size());
        assertEquals("Login with John Smith and secret", resolvedValues.get().get(0).toString());
    }

    @Test
    public void checkResolveCompositeValues(){
        ScalarVariable name = new ScalarVariable();
        name.addElement("John Smith");

        ScalarVariable environment = new ScalarVariable();
        environment.addElement("testing");

        ScalarVariable passwordProduction = new ScalarVariable();
        passwordProduction.addElement("secret1");

        ScalarVariable passwordTesting = new ScalarVariable();
        passwordTesting.addElement("secret2");

        Value value = new Value("Login with ${name} and ${password-${ENV}}");
        value.setVariable("${name}", name);
        value.setVariable("${ENV}", environment);
        value.setVariable("${password-testing}", passwordTesting);
        value.setVariable("${password-production}", passwordProduction);

        Optional<List<Value>> resolvedValues = value.getResolvedValues();

        assertTrue(resolvedValues.isPresent());
        assertEquals(1, resolvedValues.get().size());
        assertEquals("Login with John Smith and secret2", resolvedValues.get().get(0).toString());
    }
}
