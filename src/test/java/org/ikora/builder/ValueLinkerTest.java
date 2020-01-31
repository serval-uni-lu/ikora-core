package org.ikora.builder;

import org.ikora.model.Token;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValueLinkerTest {
    @Test
    void testSimpleMatch(){
        Token left = Token.fromString("Input password");
        Token right = Token.fromString("Input password");

        assertTrue(ValueLinker.matches(left, right));
    }

    @Test
    void testVariableMatch(){
        Token left = Token.fromString("Login \"${user}\" with password \"${password}\"");
        Token right = Token.fromString("Login \"admin\" with password \"1234\"");

        assertTrue(ValueLinker.matches(left, right));

        left = Token.fromString("Cancel of withdraw of <${amount}> in USD : creditor <${name_creditor}> account # <${acount_number_creditor}> - beneficiary <${name_beneficiary}> account # <${account_number_beneficiary}>");
        right = Token.fromString("Cancel of withdraw of <2.500,00> in USD : creditor <John> account # <LU00 1234 5678 9123 0000> - beneficiary <Jane> account # <LU22 4321 8765 3219 0000>");

        assertTrue(ValueLinker.matches(left, right));

        left = Token.fromString("N° Compte: <${account_number}>");
        right = Token.fromString("N° Compte: <LU00 1234 5678 9123 0000>");

        assertTrue(ValueLinker.matches(left, right));
    }

    @Test
    void testFindVariables(){
        Token raw = Token.fromString("Login \"@{user}\" with password \"${password}\" with options \"&{options}\"");
        List<Token> variables = ValueLinker.findVariables(raw);

        assertEquals(3, variables.size());
        assertEquals("@{user}", variables.get(0).getText());
        assertEquals("${password}", variables.get(1).getText());
        assertEquals("&{options}", variables.get(2).getText());
    }

    @Test
    void testIsVariable() {
        Token scalar = Token.fromString("${ScalarVariable}");
        assertTrue(ValueLinker.isVariable(scalar));

        Token list = Token.fromString("@{ListVariable}");
        assertTrue(ValueLinker.isVariable(list));

        Token dict = Token.fromString("&{DictVariable}");
        assertTrue(ValueLinker.isVariable(dict));

        Token regularString = Token.fromString("String");
        assertFalse(ValueLinker.isVariable(regularString));
    }

    @Test
    void testHasVariable() {
        Token scalar = Token.fromString("Some text \"${ScalarVariable}\" More text");
        assertTrue(ValueLinker.hasVariable(scalar));

        Token list = Token.fromString("Some text \"@{ListVariable}\" More text");
        assertTrue(ValueLinker.hasVariable(list));

        Token dict = Token.fromString("Some text \"&{DictVariable}\" More text");
        assertTrue(ValueLinker.hasVariable(dict));

        Token regularString = Token.fromString("String with some text");
        assertFalse(ValueLinker.hasVariable(regularString));
    }

 /*
    @Test
    void checkResolvedSimpleValues() throws InvalidDependencyException {
        ScalarVariable name = new ScalarVariable(Token.fromString("${name}"));
        name.addArgument(Token.fromString("John Smith"));

        ScalarVariable password = new ScalarVariable(Token.fromString("${password}"));
        password.addElement(Token.fromString("secret"));

        Value value = new Value(Token.fromString("Login with ${name} and ${password}"));
        value.setVariable(Token.fromString("${name}"), name);
        value.setVariable(Token.fromString("${password}"), password);

        Optional<List<Value>> resolvedValues = value.getResolvedValues();

        assertTrue(resolvedValues.isPresent());
        assertEquals(1, resolvedValues.get().size());
        assertEquals("Login with John Smith and secret", resolvedValues.get().get(0).toString());
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
*/

    @Test
    void testScalarBareNameExtraction(){
        String scalar = "${scalar}";
        String bareScalar = ValueLinker.getBareVariableName(scalar);
        assertEquals("scalar", bareScalar);
    }

    @Test
    void checkListBareNameExtraction(){
        String list = "@{list}";
        String bareList = ValueLinker.getBareVariableName(list);
        assertEquals("list", bareList);
    }

    @Test
    void checkDictionaryBareNameExtraction(){
        String dictionary = "${dictionary}";
        String bareDictionary = ValueLinker.getBareVariableName(dictionary);
        assertEquals("dictionary", bareDictionary);
    }

    @Test
    void checkVariableWithSpaceToGeneric(){
        String variable = "this is a variable with space";
        String generic = ValueLinker.getGenericVariableName(variable);
        assertEquals("thisisavariablewithspace", generic);
    }

    @Test
    void checkVariableWithUnderscoreToGeneric(){
        String variable = "this_is_a_variable_with_underscore";
        String generic = ValueLinker.getGenericVariableName(variable);
        assertEquals("thisisavariablewithunderscore", generic);
    }

    @Test
    void checkCompositeBareNameExtraction(){
        String composite = "${ENV-${env}}";
        String bareComposite = ValueLinker.getBareVariableName(composite);
        assertEquals("ENV-${env}", bareComposite);
    }

    @Test
    void checkEscapeParenthesis(){
        String simple = "${simple}";
        String parenthesis = "${test(with_parenthesis)}";

        assertEquals("\\$\\{simple\\}", ValueLinker.escape(simple));
        assertEquals("\\$\\{test\\(with_parenthesis\\)\\}", ValueLinker.escape(parenthesis));
    }

    @Test
    void checkEscapeDash(){
        String dash = "${30-0931-450-32}";
        assertEquals("\\$\\{30\\-0931\\-450\\-32\\}", ValueLinker.escape(dash));
    }
}
