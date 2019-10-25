package org.ukwikora.model;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ValueTest {
    @Test
    void checkSimpleMatch(){
        Value value = new Value("Input password");
        String test = "Input password";

        assertTrue(value.matches(test));
    }

    @Test
    void checkVariableMatch(){
        Value value = new Value("Login \"${user}\" with password \"${password}\"");
        String test = "Login \"admin\" with password \"1234\"";
        assertTrue(value.matches(test));

        value = new Value("Cancel of withdraw of <${amount}> in USD : creditor <${name_creditor}> account # <${acount_number_creditor}> - beneficiary <${name_beneficiary}> account # <${account_number_beneficiary}>");
        test = "Cancel of withdraw of <2.500,00> in USD : creditor <John> account # <LU00 1234 5678 9123 0000> - beneficiary <Jane> account # <LU22 4321 8765 3219 0000>";
        assertTrue(value.matches(test));

        value = new Value("N° Compte: <${account_number}>");
        test = "N° Compte: <LU00 1234 5678 9123 0000>";
        assertTrue(value.matches(test));
    }

    @Test
    void checkFindVariables(){
        String raw = "Login \"@{user}\" with password \"${password}\" with options \"&{options}\"";
        List<String> variables = Value.findVariables(raw);

        assertEquals(3, variables.size());
        assertEquals("@{user}", variables.get(0));
        assertEquals("${password}", variables.get(1));
        assertEquals("&{options}", variables.get(2));
    }

    @Test
    void checkIsVariable() {
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
    void checkHasVariable() {
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
    void checkResolvedSimpleValues(){
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
    void checkResolveCompositeValues(){
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
    void checkScalarBareNameExtraction(){
        String scalar = "${scalar}";
        String bareScalar = Value.getBareVariableName(scalar);
        assertEquals("scalar", bareScalar);
    }

    @Test
    void checkListBareNameExtraction(){
        String list = "@{list}";
        String bareList = Value.getBareVariableName(list);
        assertEquals("list", bareList);
    }

    @Test
    void checkDictionaryBareNameExtraction(){
        String dictionary = "${dictionary}";
        String bareDictionary = Value.getBareVariableName(dictionary);
        assertEquals("dictionary", bareDictionary);
    }

    @Test
    void checkVariableWithSpaceToGeneric(){
        String variable = "this is a variable with space";
        String generic = Value.getGenericVariableName(variable);
        assertEquals("thisisavariablewithspace", generic);
    }

    @Test
    void checkVariableWithUnderscoreToGeneric(){
        String variable = "this_is_a_variable_with_underscore";
        String generic = Value.getGenericVariableName(variable);
        assertEquals("thisisavariablewithunderscore", generic);
    }

    @Test
    void checkCompositeBareNameExtraction(){
        String composite = "${ENV-${env}}";
        String bareComposite = Value.getBareVariableName(composite);
        assertEquals("ENV-${env}", bareComposite);
    }

    @Test
    void checkEscapeParenthesis(){
        String simple = "${simple}";
        String parenthesis = "${test(with_parenthesis)}";

        assertEquals("\\$\\{simple\\}", Value.escape(simple));
        assertEquals("\\$\\{test\\(with_parenthesis\\)\\}", Value.escape(parenthesis));
    }

    @Test
    void checkEscapeDash(){
        String dash = "${30-0931-450-32}";
        assertEquals("\\$\\{30\\-0931\\-450\\-32\\}", Value.escape(dash));
    }
}
