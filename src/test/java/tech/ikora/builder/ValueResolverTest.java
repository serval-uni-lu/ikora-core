package tech.ikora.builder;

import tech.ikora.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ValueResolverTest {
    @Test
    void testSimpleMatch(){
        Token left = Token.fromString("Input password");
        Token right = Token.fromString("Input password");

        assertTrue(ValueResolver.matches(left, right));
    }

    @Test
    void testVariableMatch(){
        Token left = Token.fromString("Login \"${user}\" with password \"${password}\"");
        Token right = Token.fromString("Login \"admin\" with password \"1234\"");

        assertTrue(ValueResolver.matches(left, right));

        left = Token.fromString("Cancel of withdraw of <${amount}> in USD : creditor <${name_creditor}> account # <${acount_number_creditor}> - beneficiary <${name_beneficiary}> account # <${account_number_beneficiary}>");
        right = Token.fromString("Cancel of withdraw of <2.500,00> in USD : creditor <John> account # <LU00 1234 5678 9123 0000> - beneficiary <Jane> account # <LU22 4321 8765 3219 0000>");

        assertTrue(ValueResolver.matches(left, right));

        left = Token.fromString("N° Compte: <${account_number}>");
        right = Token.fromString("N° Compte: <LU00 1234 5678 9123 0000>");

        assertTrue(ValueResolver.matches(left, right));
    }

    @Test
    void testFindVariables(){
        Token raw = Token.fromString("Login \"@{user}\" with password \"${password}\" with options \"&{options}\"");
        List<Token> variables = ValueResolver.findVariables(raw);

        assertEquals(3, variables.size());
        assertEquals("@{user}", variables.get(0).getText());
        assertEquals("${password}", variables.get(1).getText());
        assertEquals("&{options}", variables.get(2).getText());
    }

    @Test
    void testIsVariable() {
        Token scalar = Token.fromString("${ScalarVariable}");
        assertTrue(ValueResolver.isVariable(scalar));

        Token list = Token.fromString("@{ListVariable}");
        assertTrue(ValueResolver.isVariable(list));

        Token dict = Token.fromString("&{DictVariable}");
        assertTrue(ValueResolver.isVariable(dict));

        Token regularString = Token.fromString("String");
        assertFalse(ValueResolver.isVariable(regularString));
    }

    @Test
    void testHasVariable() {
        Token scalar = Token.fromString("Some text \"${ScalarVariable}\" More text");
        assertTrue(ValueResolver.hasVariable(scalar));

        Token list = Token.fromString("Some text \"@{ListVariable}\" More text");
        assertTrue(ValueResolver.hasVariable(list));

        Token dict = Token.fromString("Some text \"&{DictVariable}\" More text");
        assertTrue(ValueResolver.hasVariable(dict));

        Token regularString = Token.fromString("String with some text");
        assertFalse(ValueResolver.hasVariable(regularString));
    }

    @Test
    void testScalarBareNameExtraction(){
        String scalar = "${scalar}";
        String bareScalar = ValueResolver.getBareVariableName(scalar);
        assertEquals("scalar", bareScalar);
    }

    @Test
    void testListBareNameExtraction(){
        String list = "@{list}";
        String bareList = ValueResolver.getBareVariableName(list);
        assertEquals("list", bareList);
    }

    @Test
    void testDictionaryBareNameExtraction(){
        String dictionary = "${dictionary}";
        String bareDictionary = ValueResolver.getBareVariableName(dictionary);
        assertEquals("dictionary", bareDictionary);
    }

    @Test
    void testVariableWithSpaceToGeneric(){
        String variable = "this is a variable with space";
        String generic = ValueResolver.getGenericVariableName(variable);
        assertEquals("thisisavariablewithspace", generic);
    }

    @Test
    void testVariableWithUnderscoreToGeneric(){
        String variable = "this_is_a_variable_with_underscore";
        String generic = ValueResolver.getGenericVariableName(variable);
        assertEquals("thisisavariablewithunderscore", generic);
    }

    @Test
    void testCompositeBareNameExtraction(){
        String composite = "${ENV-${env}}";
        String bareComposite = ValueResolver.getBareVariableName(composite);
        assertEquals("ENV-${env}", bareComposite);
    }

    @Test
    void testEscapeParenthesis(){
        String simple = "${simple}";
        String parenthesis = "${test(with_parenthesis)}";

        assertEquals("\\$\\{simple\\}", ValueResolver.escape(simple));
        assertEquals("\\$\\{test\\(with_parenthesis\\)\\}", ValueResolver.escape(parenthesis));
    }

    @Test
    void testEscapeDash(){
        String dash = "${30-0931-450-32}";
        assertEquals("\\$\\{30\\-0931\\-450\\-32\\}", ValueResolver.escape(dash));
    }

    @Test
    void testGetValuesFromVariableTable() {
       final String code =
               "*** Keywords ***\n" +
               "From direct assignment\n" +
               "    Log  ${variable}\n" +
               "\n" +
               "*** Variables ***\n" +
               " ${variable}  Some variable";

        final BuildResult result = Builder.build(code, true);

        final Project project = result.getProjects().iterator().next();

        final Set<UserKeyword> ofInterest = project.findUserKeyword(Token.fromString("From direct assignment"));
        final UserKeyword keyword = ofInterest.iterator().next();

        final KeywordCall call = (KeywordCall)keyword.getStep(0);
        final Argument argument = call.getArgumentList().get(0);

        final SourceNode value = argument.getDefinition();

        final Set<Node> definitions = ((Variable) value).getDefinition(Link.Import.BOTH);
        final Node definition = definitions.iterator().next();

        assertTrue(definition instanceof VariableAssignment);
    }

    @Test
    void testGetValuesFromKeywordParameters() {
        final String code =
                "*** Test Cases ***\n" +
                "Simple call\n" +
                "    From parameter assignment    the value\n" +
                "\n" +
                "*** Keywords ***\n" +
                "From parameter assignment\n" +
                "    [Arguments]    ${local}\n" +
                "    Log    ${local}\n" +
                "\n" +
                "*** Variables ***\n" +
                " ${variable}  Some variable";

        final BuildResult result = Builder.build(code, true);

        final Project project = result.getProjects().iterator().next();

        final Set<UserKeyword> ofInterest = project.findUserKeyword(Token.fromString("From parameter assignment"));
        final UserKeyword keyword = ofInterest.iterator().next();

        final KeywordCall call = (KeywordCall)keyword.getStep(0);
        final Argument argument = call.getArgumentList().get(0);

        final SourceNode value = argument.getDefinition();

        final Set<Node> definitions = ((Variable) value).getDefinition(Link.Import.BOTH);
        final Node definition = definitions.iterator().next();

        assertTrue(definition instanceof ScalarVariable);

        final SourceNode astParent = ((ScalarVariable) definition).getAstParent().getAstParent();
        assertTrue(astParent instanceof UserKeyword);
    }

    @Test
    void testGetValuesFromKeywordAssignment() {
        final String code =
                "*** Keywords ***\n" +
                "\n" +
                "Test with a simple test case to see how assignment works\n" +
                "    [Arguments]  ${user}    ${pwd}\n" +
                "    ${EtatRun}=    Run Keyword And Return Status    Connexion to the service    ${user}    ${pwd}\n" +
                "    Log    ${EtatRun}\n" +
                "\n" +
                "Connexion to the service\n" +
                "    [Arguments]  ${user}    ${pwd}\n" +
                "    Log    Connecting to service with ${user} and ${pwd}";

        final BuildResult result = Builder.build(code, true);

        final Project project = result.getProjects().iterator().next();

        final Set<UserKeyword> ofInterest = project.findUserKeyword(Token.fromString("Test with a simple test case to see how assignment works"));
        final UserKeyword keyword = ofInterest.iterator().next();

        final KeywordCall call = (KeywordCall)keyword.getStep(1);
        final Argument argument = call.getArgumentList().get(0);

        final SourceNode value = argument.getDefinition();

        final Set<Node> definitions = ((Variable) value).getDefinition(Link.Import.BOTH);
        final Node definition = definitions.iterator().next();

        assertTrue(definition instanceof ScalarVariable);

        final SourceNode astParent = ((ScalarVariable) definition).getAstParent().getAstParent();
        assertTrue(astParent instanceof Assignment);
    }
}
