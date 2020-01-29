package org.ikora.model;

import org.ikora.exception.InvalidDependencyException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ValueTest {
    @Test
    void checkSimpleMatch(){
        Value value = new Value(Token.fromString("Input password"));
        Token test = Token.fromString("Input password");

        assertTrue(value.matches(test));
    }

    @Test
    void checkVariableMatch(){
        Value value = new Value(Token.fromString("Login \"${user}\" with password \"${password}\""));
        Token test = Token.fromString("Login \"admin\" with password \"1234\"");
        assertTrue(value.matches(test));

        value = new Value(Token.fromString("Cancel of withdraw of <${amount}> in USD : creditor <${name_creditor}> account # <${acount_number_creditor}> - beneficiary <${name_beneficiary}> account # <${account_number_beneficiary}>"));
        test = Token.fromString("Cancel of withdraw of <2.500,00> in USD : creditor <John> account # <LU00 1234 5678 9123 0000> - beneficiary <Jane> account # <LU22 4321 8765 3219 0000>");
        assertTrue(value.matches(test));

        value = new Value(Token.fromString("N° Compte: <${account_number}>"));
        test = Token.fromString("N° Compte: <LU00 1234 5678 9123 0000>");
        assertTrue(value.matches(test));
    }

    @Test
    void checkFindVariables(){
        Token raw = Token.fromString("Login \"@{user}\" with password \"${password}\" with options \"&{options}\"");
        List<Token> variables = Value.findVariables(raw);

        assertEquals(3, variables.size());
        assertEquals("@{user}", variables.get(0).getValue());
        assertEquals("${password}", variables.get(1).getValue());
        assertEquals("&{options}", variables.get(2).getValue());
    }

    @Test
    void checkIsVariable() {
        Token scalar = Token.fromString("${ScalarVariable}");
        assertTrue(Value.isVariable(scalar));

        Token list = Token.fromString("@{ListVariable}");
        assertTrue(Value.isVariable(list));

        Token dict = Token.fromString("&{DictVariable}");
        assertTrue(Value.isVariable(dict));

        Token regularString = Token.fromString("String");
        assertFalse(Value.isVariable(regularString));
    }

    @Test
    void checkHasVariable() {
        Token scalar = Token.fromString("Some text \"${ScalarVariable}\" More text");
        assertTrue(Value.hasVariable(scalar));

        Token list = Token.fromString("Some text \"@{ListVariable}\" More text");
        assertTrue(Value.hasVariable(list));

        Token dict = Token.fromString("Some text \"&{DictVariable}\" More text");
        assertTrue(Value.hasVariable(dict));

        Token regularString = Token.fromString("String with some text");
        assertFalse(Value.hasVariable(regularString));
    }

    @Test
    void checkResolvedSimpleValues() throws InvalidDependencyException {
        ScalarVariable name = new ScalarVariable(Token.fromString("${name}"));
        name.addElement(Token.fromString("John Smith"));

        ScalarVariable password = new ScalarVariable(Token.fromString("${password}"));
        password.addElement(Token.fromString("secret"));

        Value value = new Value(Token.fromString("Login with ${name} and ${password}"));
        value.setVariable(Token.fromString("${name}"), name);
        value.setVariable(Token.fromString("${password}"), password);

        //Optional<List<Value>> resolvedValues = value.getResolvedValues();

        //assertTrue(resolvedValues.isPresent());
        //assertEquals(1, resolvedValues.get().size());
        //assertEquals("Login with John Smith and secret", resolvedValues.get().get(0).toString());
    }

    @Test
    void checkResolveCompositeValues() throws InvalidDependencyException {
        ScalarVariable name = new ScalarVariable(Token.fromString("${name}"));
        name.addElement(Token.fromString("John Smith"));

        ScalarVariable environment = new ScalarVariable(Token.fromString("${ENV}"));
        environment.addElement(Token.fromString("testing"));

        ScalarVariable passwordProduction = new ScalarVariable(Token.fromString("${password-testing}"));
        passwordProduction.addElement(Token.fromString("secret1"));

        ScalarVariable passwordTesting = new ScalarVariable(Token.fromString("${password-production}"));
        passwordTesting.addElement(Token.fromString("secret2"));

        Value value = new Value(Token.fromString("Login with ${name} and ${password-${ENV}}"));
        value.setVariable(Token.fromString("${name}"), name);
        value.setVariable(Token.fromString("${ENV}"), environment);
        value.setVariable(Token.fromString("${password-testing}"), passwordTesting);
        value.setVariable(Token.fromString("${password-production}"), passwordProduction);

        //Optional<List<Value>> resolvedValues = value.getResolvedValues();

        //assertTrue(resolvedValues.isPresent());
        //assertEquals(1, resolvedValues.get().size());
        //assertEquals("Login with John Smith and secret2", resolvedValues.get().get(0).toString());
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
