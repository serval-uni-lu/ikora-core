package tech.ikora.analytics.visitor;

import org.junit.jupiter.api.Test;
import tech.ikora.builder.BuildResult;
import tech.ikora.builder.Builder;
import tech.ikora.model.Project;
import tech.ikora.model.TestCase;

import static org.junit.jupiter.api.Assertions.*;

class UserKeywordCounterVisitorTest {
    @Test
    void testVisitingNodeCalledManyTimesWithPathMemory(){
        final String code =
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    User \"demo\" logs in with password \"mode\"\n" +
                "    User \"Bob\" logs in with password \"password\"\n" +
                "\n" +
                "*** Keywords ***\n" +
                "\n" +
                "User \"demo\" logs in with password \"mode\"\n" +
                "    Input username    demo\n" +
                "    Input password    mode\n" +
                "    Submit credentials\n" +
                "\n" +
                "User \"Bob\" logs in with password \"password\"\n" +
                "    Input username    Bob\n" +
                "    Input password    password\n" +
                "    Submit credentials\n" +
                "\n" +
                "Input Username\n" +
                "    [Arguments]    ${username}\n" +
                "    Input Text    ${USERNAME_FIELD}    ${username}\n" +
                "\n" +
                "Input Password\n" +
                "    [Arguments]    ${password}\n" +
                "    Input Text    password_field    ${password}\n" +
                "\n" +
                "Submit Credentials\n" +
                "    Click Button    ${BOTTON_FIELD}\n" +
                "\n" +
                "*** Variables ***\n" +
                "${USERNAME_FIELD}      username_field\n" +
                "${BOTTON_FIELD}        login_button\n";

        final BuildResult result = Builder.build(code, true);
        final Project project = result.getProjects().iterator().next();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        assertNotNull(testCase);

        UserKeywordCounterVisitor visitor = new UserKeywordCounterVisitor();
        visitor.visit(testCase, new PathMemory());

        assertEquals(5, visitor.getCount());
    }
}