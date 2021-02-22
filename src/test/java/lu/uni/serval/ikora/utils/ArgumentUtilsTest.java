package lu.uni.serval.ikora.utils;

import lu.uni.serval.ikora.builder.BuildResult;
import lu.uni.serval.ikora.builder.Builder;
import lu.uni.serval.ikora.model.Argument;
import lu.uni.serval.ikora.model.NodeList;
import lu.uni.serval.ikora.model.Project;
import lu.uni.serval.ikora.model.TestCase;
import lu.uni.serval.ikora.types.ListType;
import lu.uni.serval.ikora.types.LocatorType;
import lu.uni.serval.ikora.types.StringType;
import lu.uni.serval.ikora.types.TimeoutType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentUtilsTest {
    @Test
    void testGetArgumentTypeForUserKeywordWithLiterals(){
        final String code =
                "*** Test Cases ***\n" +
                "\n" +
                "Test connexion\n" +
                "    Connexion to the service    username    password\n" +
                "\n" +
                "*** Keywords ***\n" +
                "\n" +
                "Connexion to the service\n" +
                "    [Arguments]  ${user}    ${pwd}\n" +
                "    Log    Connecting to service with ${user} and ${pwd}";

        final BuildResult result = Builder.build(code, true);
        final Project project = result.getProjects().iterator().next();
        final TestCase testCase = project.findTestCase("", "Test connexion").iterator().next();
        final NodeList<Argument> argumentList = testCase.getStep(0).getArgumentList();

        assertEquals(StringType.class, ArgumentUtils.getArgumentType(argumentList.get(0)));
        assertEquals(StringType.class, ArgumentUtils.getArgumentType(argumentList.get(1)));
    }

    @Test
    void testGetArgumentTypeForUserKeywordWithList(){
        final String code =
                "*** Test Cases ***\n" +
                        "\n" +
                        "Test connexion\n" +
                        "    @{credentials}=    Create List    username    password\n" +
                        "    Connexion to the service    @{credentials}\n" +
                        "\n" +
                        "*** Keywords ***\n" +
                        "\n" +
                        "Connexion to the service\n" +
                        "    [Arguments]  ${user}    ${pwd}\n" +
                        "    Log    Connecting to service with ${user} and ${pwd}";

        final BuildResult result = Builder.build(code, true);
        final Project project = result.getProjects().iterator().next();
        final TestCase testCase = project.findTestCase("", "Test connexion").iterator().next();
        final NodeList<Argument> argumentList = testCase.getStep(0).getArgumentList();

        assertEquals(ListType.class, ArgumentUtils.getArgumentType(argumentList.get(0)));
    }

    @Test
    void testGetArgumentTypeForLibraryKeywordWithLocator(){
        final String code =
                "*** Test Cases ***\n" +
                        "\n" +
                        "Test click button\n" +
                        "    Click Button    button_id\n";

        final BuildResult result = Builder.build(code, true);
        final Project project = result.getProjects().iterator().next();
        final TestCase testCase = project.findTestCase("", "Test click button").iterator().next();
        final NodeList<Argument> argumentList = testCase.getStep(0).getArgumentList();

        assertEquals(LocatorType.class, ArgumentUtils.getArgumentType(argumentList.get(0)));
    }

    @Test
    void testGetArgumentTypeForLibraryKeywordWithTimeout(){
        final String code =
                "*** Test Cases ***\n" +
                        "\n" +
                        "Test sleep\n" +
                        "    Sleep    10 seconds\n";

        final BuildResult result = Builder.build(code, true);
        final Project project = result.getProjects().iterator().next();
        final TestCase testCase = project.findTestCase("", "Test sleep").iterator().next();
        final NodeList<Argument> argumentList = testCase.getStep(0).getArgumentList();

        assertEquals(TimeoutType.class, ArgumentUtils.getArgumentType(argumentList.get(0)));
    }

}