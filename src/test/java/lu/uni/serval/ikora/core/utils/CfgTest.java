package lu.uni.serval.ikora.core.utils;

import lu.uni.serval.ikora.core.builder.BuildResult;
import lu.uni.serval.ikora.core.builder.Builder;
import lu.uni.serval.ikora.core.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CfgTest {
    private final static Project loginProject;

    static {
        final String loginCode =
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    Given browser is opened to login page\n" +
                "    When user \"demo\" logs in with password \"mode\"\n" +
                "    Then welcome page should be open\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Browser is opened to login page\n" +
                "    Open browser to login page\n" +
                "\n" +
                "User \"${username}\" logs in with password \"${password}\"\n" +
                "    Input username    ${username}\n" +
                "    Input password    ${password}\n" +
                "    Submit credentials\n" +
                "\n" +
                "Open Browser To Login Page\n" +
                "    Open Browser    ${LOGIN URL}    ${BROWSER}\n" +
                "    Maximize Browser Window\n" +
                "    Sleep 5\n" +
                "    Login Page Should Be Open\n" +
                "\n" +
                "Login Page Should Be Open\n" +
                "    Title Should Be    Login Page\n" +
                "\n" +
                "Go To Login Page\n" +
                "    Go To    ${LOGIN URL}\n" +
                "    Login Page Should Be Open\n" +
                "\n" +
                "Input Username\n" +
                "    [Arguments]    ${username}\n" +
                "    Input Text    username_field    ${username}\n" +
                "\n" +
                "Input Password\n" +
                "    [Arguments]    ${password}\n" +
                "    Input Text    ${PASSWORD_FIELD}    ${password}\n" +
                "\n" +
                "Submit Credentials\n" +
                "    Click Button    login_button\n" +
                "\n" +
                "Welcome Page Should Be Open\n" +
                "    Location Should Be    ${WELCOME URL}\n" +
                "    Title Should Be    Welcome Page\n" +
                "\n" +
                "*** Variables ***\n" +
                "${SERVER}         localhost:7272\n" +
                "${BROWSER}        Firefox\n" +
                "${DELAY}          0\n" +
                "${VALID USER}     demo\n" +
                "${VALID PASSWORD}    mode\n" +
                "${LOGIN URL}      http://${SERVER}/\n" +
                "${WELCOME URL}    http://${SERVER}/welcome.html\n" +
                "${ERROR URL}      http://${SERVER}/error.html\n" +
                "${PASSWORD_FIELD}      password_field\n";

        final BuildResult build = Builder.build(loginCode, true);
        loginProject = build.getProjects().iterator().next();

    }


    @Test
    void testIsCalledWithUserKeyword(){
        final UserKeyword userLogin = loginProject.findUserKeyword(FileUtils.IN_MEMORY, "User \"${username}\" logs in with password \"${password}\"")
                .iterator().next();

        final Step clickButton = loginProject.findUserKeyword(FileUtils.IN_MEMORY, "Submit Credentials")
                .iterator().next()
                .getStep(0);

        assertTrue(Cfg.isCalledBy(clickButton, userLogin));
    }

    @Test
    void testIsCalledWithTestCase(){
        final TestCase validLogin = loginProject.findTestCase(FileUtils.IN_MEMORY, "Valid Login")
                .iterator().next();

        final Step clickButton = loginProject.findUserKeyword(FileUtils.IN_MEMORY, "Submit Credentials")
                .iterator().next()
                .getStep(0);

        assertTrue(Cfg.isCalledBy(clickButton, validLogin));
    }

    @Test
    void testIsCalledFromVariable(){
        final TestCase validLogin = loginProject.findTestCase(FileUtils.IN_MEMORY, "Valid Login")
                .iterator().next();

        final Argument loginButton = loginProject.findUserKeyword(FileUtils.IN_MEMORY, "Submit Credentials")
                .iterator().next()
                .getStep(0)
                .getArgumentList().get(0);

        assertTrue(Cfg.isCalledBy(loginButton, validLogin));
    }

    @Test
    void testIsCalledFromVariableAssignment(){
        final TestCase validLogin = loginProject.findTestCase(FileUtils.IN_MEMORY, "Valid Login")
                .iterator().next();

        final VariableAssignment server = loginProject.findVariable(FileUtils.IN_MEMORY, "${PASSWORD_FIELD}")
                .iterator().next();

        assertTrue(Cfg.isCalledBy(server, validLogin));
    }

    @Test
    void testIsCalledFromVariableAssignmentValue(){
        final TestCase validLogin = loginProject.findTestCase(FileUtils.IN_MEMORY, "Valid Login")
                .iterator().next();

        final VariableAssignment password = loginProject.findVariable(FileUtils.IN_MEMORY, "${PASSWORD_FIELD}")
                .iterator().next();

        final SourceNode passwordField = password.getValues().get(0).getDefinition();


        assertTrue(Cfg.isCalledBy(passwordField, validLogin));
    }
}
