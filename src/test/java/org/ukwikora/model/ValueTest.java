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
        ScalarVariable name = new ScalarVariable("${name}");
        name.addElement("John Smith");

        ScalarVariable password = new ScalarVariable("${password}");
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
        ScalarVariable name = new ScalarVariable("${name}");
        name.addElement("John Smith");

        ScalarVariable environment = new ScalarVariable("${ENV}");
        environment.addElement("testing");

        ScalarVariable passwordProduction = new ScalarVariable("${password-testing}");
        passwordProduction.addElement("secret1");

        ScalarVariable passwordTesting = new ScalarVariable("${password-production}");
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

    @Test
    public void checkScalarBareNameExtraction(){
        String scalar = "${scalar}";
        String bareScalar = Value.getBareName(scalar);
        assertEquals("scalar", bareScalar);
    }

    @Test
    public void checkListBareNameExtraction(){
        String list = "@{list}";
        String bareList = Value.getBareName(list);
        assertEquals("list", bareList);
    }

    @Test
    public void checkDictionaryBareNameExtraction(){
        String dictionary = "${dictionary}";
        String bareDictionary = Value.getBareName(dictionary);
        assertEquals("dictionary", bareDictionary);
    }

    @Test
    public void checkCompositeBareNameExtraction(){
        String composite = "${ENV-${env}}";
        String bareComposite = Value.getBareName(composite);
        assertEquals("ENV-${env}", bareComposite);
    }

    @Test
    public void checkEscape(){
        String simple = "${simple}";
        String parenthesis = "${test(with_parenthesis)}";

        assertEquals("\\$\\{simple\\}", Value.escape(simple));
        assertEquals("\\$\\{test\\(with_parenthesis\\)\\}", Value.escape(parenthesis));
    }
}
