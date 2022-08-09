/*
 *
 *     Copyright © 2019 - 2022 University of Luxembourg
 *
 *     Licensed under the Apache License, Version 2.0 (the "License")
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package lu.uni.serval.ikora.core.builder;

import lu.uni.serval.ikora.core.analytics.resolver.ValueResolver;
import lu.uni.serval.ikora.core.error.ErrorMessages;
import lu.uni.serval.ikora.core.error.SyntaxError;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.types.ConditionType;
import lu.uni.serval.ikora.core.types.KeywordType;
import lu.uni.serval.ikora.core.types.UnresolvedType;
import lu.uni.serval.ikora.core.utils.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import lu.uni.serval.ikora.core.Helpers;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BuilderTest {
    @Test
    void testParseLibraryVariable(){
        final String code =
                "*** Test Cases ***\n" +
                "Environment variables\n" +
                "    Log    Test Status: ${TEST STATUS}";

        final BuildResult result = Builder.build(code, true);
        Assertions.assertEquals(1, result.getProjects().size());

        final Project project = result.getProjects().iterator().next();
        assertEquals(1, project.getTestCases().size());

        final TestCase testCase = project.findTestCase(FileUtils.IN_MEMORY, "Environment variables").iterator().next();
        assertEquals(1, testCase.getSteps().size());
        assertEquals(2, testCase.getRange().getStart().getLine());

        final KeywordCall step = (KeywordCall)testCase.getSteps().get(0);
        assertEquals(1, step.getArgumentList().size());

        final Argument argument = step.getArgumentList().get(0);
        assertTrue(argument.getName().contains("Test Status"));
        assertNotNull(argument.getAstParent());
    }

    @Test
    void testBuildWithMultilineKeywordCall(){
        final String code =
                "*** Test Cases ***\n" +
                "Environment variables\n" +
                "    Log\n" +
                "    ...    Test Status: ${TEST STATUS}";

        final BuildResult result = Builder.build(code, true);
        final Project project = result.getProjects().iterator().next();
        final TestCase testCase = project.findTestCase(FileUtils.IN_MEMORY, "Environment variables").iterator().next();

        final KeywordCall step = (KeywordCall)testCase.getSteps().get(0);
        assertEquals(1, step.getArgumentList().size());
    }

    @Test
    void testBuildWithValueForVariableContainingDot() {
        final String code =
                "*** Test Cases ***\n" +
                "001 - Execute\n" +
                "    Show the content of 45.6 is a value with a dot in it\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Show the content of ${value}\n" +
                "    Log    ${value}\n";

        final BuildResult result = Builder.build(code, true);
        Assertions.assertEquals(1, result.getProjects().size());

        final Project project = result.getProjects().iterator().next();

        final Set<UserKeyword> keywords = project.findUserKeyword(Token.fromString("Show the content of ${value}"));
        assertEquals(1, keywords.size());

        final TestCase testCase = project.findTestCase(FileUtils.IN_MEMORY, "001 - Execute").iterator().next();
        assertEquals(1, testCase.getSteps().size());

        final Optional<SourceFile> sourceFile = project.getSourceFile(FileUtils.IN_MEMORY);
        assertTrue(sourceFile.isPresent());

        assertEquals(8, sourceFile.get().getTokens().size());
    }

    @Test
    void testScopedByPrefixResolution() throws IOException, URISyntaxException {
        final File robot = FileUtils.getResourceFile("robot/scope-testing");
        assertNotNull(robot);

        final BuildResult result = Builder.build(robot, Helpers.getConfiguration(), true);
        Assertions.assertEquals(1, result.getProjects().size());
        Assertions.assertTrue(result.getErrors().isEmpty());

        final Project project = result.getProjects().iterator().next();

        Set<UserKeyword> fromResources1 = project.findUserKeyword(Token.fromString("Load keyword from resource1"));
        assertEquals(1, fromResources1.size());

        Optional<Keyword> resources1Step0 = ((KeywordCall)fromResources1.iterator().next().getStep(0)).getKeyword();
        assertTrue(resources1Step0.isPresent());
        assertEquals("resources1.robot", resources1Step0.get().getLibraryName());

        Set<UserKeyword> fromResources2 = project.findUserKeyword(Token.fromString("Load keyword from resource2"));
        assertEquals(1, fromResources2.size());

        Optional<Keyword> resources2Step0 = ((KeywordCall)fromResources2.iterator().next().getStep(0)).getKeyword();
        assertTrue(resources2Step0.isPresent());
        assertEquals("resources2.robot", resources2Step0.get().getLibraryName());
    }

    @Test
    void testDuplicatedStaticallyImportedKeywordsAreDetectedTwice(){
        File robot = null;

        try{
        robot = FileUtils.getResourceFile("robot/duplicated-keyword");
        } catch (Exception e) {
            fail(String.format("Failed to load 'robot/scope-testing' from resources: %s", e.getMessage()));
        }

        final BuildResult result = Builder.build(robot, Helpers.getConfiguration(), true);
        Assertions.assertEquals(1, result.getProjects().size());

        final Project project = result.getProjects().iterator().next();

        Set<TestCase> mainTest = project.getTestCases();
        assertEquals(1, mainTest.size());

        KeywordCall simpleCall = (KeywordCall) mainTest.iterator().next().getStep(0);
        assertNotNull(simpleCall);
        assertEquals(1, simpleCall.getAllPotentialKeywords(Link.Import.BOTH).size());

        KeywordCall duplicateCall = (KeywordCall) mainTest.iterator().next().getStep(1);
        assertNotNull(duplicateCall);
        assertEquals(2, duplicateCall.getAllPotentialKeywords(Link.Import.BOTH).size());
    }

    @Test
    void testKeywordWithArguments(){
        final String code =
                "*** Test Cases ****\n" +
                "Some Test\n" +
                "    Connexion to the service    username    password\n" +
                "*** Keywords ***\n" +
                "Connexion to the service\n" +
                "    [Arguments]  ${user}    ${pwd}\n" +
                "    Log    ${user}";

        final BuildResult result = Builder.build(code, true);
        Assertions.assertEquals(1, result.getProjects().size());

        final Project project = result.getProjects().iterator().next();

        final Set<UserKeyword> userKeywords = project.getUserKeywords();
        assertEquals(1, userKeywords.size());

        final Set<UserKeyword> ofInterest = project.findUserKeyword(Token.fromString("Connexion to the service"));
        assertEquals(1, ofInterest.size());

        final UserKeyword keyword = ofInterest.iterator().next();
        final Step step0 = keyword.getStep(0);
        final List<Node> valueNodes = ValueResolver.getValueNodes(step0.getArgumentList().get(0));

        assertEquals(1, valueNodes.size());
    }

    @Test
    void testAssignmentFromKeyword() {
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
        Assertions.assertEquals(1, result.getProjects().size());

        final Project project = result.getProjects().iterator().next();

        final Set<UserKeyword> userKeywords = project.getUserKeywords();
        assertEquals(2, userKeywords.size());

        final Set<UserKeyword> ofInterest = project.findUserKeyword(Token.fromString("Test with a simple test case to see how assignment works"));
        assertEquals(1, ofInterest.size());

        final UserKeyword keyword = ofInterest.iterator().next();
        final Step step0 = keyword.getStep(0);

        assertTrue(Assignment.class.isAssignableFrom(step0.getClass()));
        final Assignment assignment = (Assignment) step0;

        assertEquals(1, assignment.getLeftHandOperand().size());
        assertEquals("${EtatRun}", assignment.getLeftHandOperand().get(0).getName());
        assertEquals(1, assignment.getDependencies().size());
        assertNotNull(assignment.getAstParent());
    }

    @Test
    void testForLoopWithRange() {
        final String code =
                "*** Keywords ***\n" +
                "Simple for loop\n" +
                "    :FOR  ${index}  IN  RANGE  1  3\n" +
                "    \\    LOG    ${index}\n" +
                "    \\    LOG    Another line";

        final BuildResult result = Builder.build(code, true);
        Assertions.assertEquals(1, result.getProjects().size());

        final Project project = result.getProjects().iterator().next();

        final Set<UserKeyword> userKeywords = project.getUserKeywords();
        assertEquals(1, userKeywords.size());

        final Set<UserKeyword> ofInterest = project.findUserKeyword(Token.fromString("Simple for loop"));
        assertEquals(1, ofInterest.size());

        final UserKeyword keyword = ofInterest.iterator().next();
        final Step step0 = keyword.getStep(0);

        assertTrue(ForLoop.class.isAssignableFrom(step0.getClass()));
        final ForLoop forLoop = (ForLoop) step0;
        assertEquals("${index}", forLoop.getIterator().getName());

        final List<Step> steps = forLoop.getSteps();
        assertEquals(2, steps.size());
    }

    @Test
    void testForLoopWithRangeWithoutTabAfterIn() {
        final String code =
                "*** Keywords ***\n" +
                "Simple for loop\n" +
                "    :FOR  ${index}  IN RANGE  1  3\n" +
                "    \\    LOG    ${index}\n" +
                "    \\    LOG    Another line";

        final BuildResult result = Builder.build(code, true);
        Assertions.assertEquals(1, result.getProjects().size());

        final Project project = result.getProjects().iterator().next();

        final Set<UserKeyword> userKeywords = project.getUserKeywords();
        assertEquals(1, userKeywords.size());

        final Set<UserKeyword> ofInterest = project.findUserKeyword(Token.fromString("Simple for loop"));
        assertEquals(1, ofInterest.size());

        final UserKeyword keyword = ofInterest.iterator().next();
        final Step step0 = keyword.getStep(0);

        assertTrue(ForLoop.class.isAssignableFrom(step0.getClass()));
        final ForLoop forLoop = (ForLoop) step0;
        assertEquals("${index}", forLoop.getIterator().getName());

        final List<Step> steps = forLoop.getSteps();
        assertEquals(2, steps.size());
    }

    @Test
    void testAssignmentFromVariableWithEqualSign() {
        final String code =
                "*** Variables ***\n" +
                "\n" +
                "${variable}=    some random value";

        final BuildResult result = Builder.build(code, true);
        Assertions.assertEquals(1, result.getProjects().size());

        final Project project = result.getProjects().iterator().next();

        Set<VariableAssignment> variables = project.getVariableAssignments();
        assertEquals(1, variables.size());
        assertNotNull(variables.iterator().next().getAstParent());
    }

    @Test
    void testTestCaseSetupWithCall() {
        final String code =
                "*** Test Cases ***\n" +
                "\n" +
                "Test with setup and teardown\n" +
                "    [Setup]  Setup the test case\n" +
                "    Do Something Cool\n" +
                "\n" +
                "*** Keywords ***\n" +
                "\n" +
                "Setup the test case\n" +
                "    Log    Setup environment\n" +
                "\n" +
                "Do Something Cool\n" +
                "    Log    Perform some cool action\n";

        final BuildResult result = Builder.build(code, true);
        assertEquals(1, result.getProjects().size());

        final Project project = result.getProjects().iterator().next();

        final Set<UserKeyword> ofInterest = project.findUserKeyword(Token.fromString("Setup the test case"));
        assertEquals(1, ofInterest.size());
        final Keyword keyword = ofInterest.iterator().next();

        final TestCase testCase = project.getTestCases().iterator().next();
        final Optional<Keyword> setup = testCase.getSetup()
                .flatMap(TestProcessing::getCall)
                .flatMap(KeywordCall::getKeyword);

        assertTrue(setup.isPresent());

        assertEquals(keyword, setup.get());
    }

    @Test
    void testTestCaseTeardownWithCall() {
        final String code =
                "*** Test Cases ***\n" +
                "\n" +
                "Test with setup and teardown\n" +
                "    Do Something Cool\n" +
                "    [Teardown]  Clean the environment\n" +
                "\n" +
                "*** Keywords ***\n" +
                "\n" +
                "Do Something Cool\n" +
                "    Log    Perform some cool action\n" +
                "\n" +
                "Clean the environment\n" +
                "    Log    Make sure nothing is polluting the environment";

        final BuildResult result = Builder.build(code, true);
        assertEquals(1, result.getProjects().size());

        final Project project = result.getProjects().iterator().next();

        final Set<UserKeyword> ofInterest = project.findUserKeyword(Token.fromString("Clean the environment"));
        assertEquals(1, ofInterest.size());
        final Keyword keyword = ofInterest.iterator().next();

        final TestCase testCase = project.getTestCases().iterator().next();
        final Optional<Keyword> teardown = testCase.getTearDown()
                .flatMap(TestProcessing::getCall)
                .flatMap(KeywordCall::getKeyword);

        assertTrue(teardown.isPresent());

        assertEquals(keyword, teardown.get());
    }

    @Test
    void testTestCaseWithSimpleTemplate() {
        final String code =
                "*** Test Cases ***\n" +
                "\n" +
                "Test Case using template with explicit arguments\n" +
                "    [Template]  This is a template\n" +
                "    Just a little\n" +
                "    A little more\n" +
                "    Are you crazy\n" +
                "\n" +
                "*** Keywords ***\n" +
                "This is a template\n" +
                "    [Arguments]  ${value}\n" +
                "    Log  ${value}";

        final BuildResult result = Builder.build(code, true);
        Assertions.assertEquals(1, result.getProjects().size());

        final Project project = result.getProjects().iterator().next();

        final TestCase testCase = project.getTestCases().iterator().next();
        assertNotNull(testCase.getTemplate());

        final Set<UserKeyword> ofInterest = project.findUserKeyword(Token.fromString("This is a template"));
        assertEquals(1, ofInterest.size());
        final Keyword keyword = ofInterest.iterator().next();

        final Step step0 = testCase.getStep(0);
        assertTrue(step0.getKeywordCall().map(KeywordCall::getKeyword).isPresent());
        final Optional<Keyword> template = step0.getKeywordCall().map(KeywordCall::getKeyword).get();
        assertTrue(template.isPresent());

        assertEquals(keyword, template.get());
    }

//    @Test
//    void testIndirectLibraryImport() throws IOException, URISyntaxException {
//        final File projectFolder = FileUtils.getResourceFile("robot/indirect-library-import");
//        assertNotNull(projectFolder);
//
//        final BuildResult result = Builder.build(projectFolder, Helpers.getConfiguration(), true);
//        final Project project = result.getProjects().iterator().next();
//        assertNotNull(project);
//
//        final UserKeyword inputPassword = project.findUserKeyword(null, "Input Password").iterator().next();
//        final Optional<KeywordCall> inputText = inputPassword.getStep(0).getKeywordCall();
//        assertTrue(inputText.isPresent());
//
//        final Optional<Keyword> keyword = inputText.get().getKeyword();
//        assertTrue(keyword.isPresent());
//    }

    @Test
    void testParseConnectedProject() throws IOException, URISyntaxException {
        final File projectAFile = FileUtils.getResourceFile("robot/connected-projects/project-a");
        assertNotNull(projectAFile);

        final File projectBFile = FileUtils.getResourceFile("robot/connected-projects/project-b");
        assertNotNull(projectBFile);

        final File projectCFile = FileUtils.getResourceFile("robot/connected-projects/project-c");
        assertNotNull(projectCFile);

        Set<File> files = new HashSet<>(Arrays.asList(projectAFile, projectBFile, projectCFile));

        final BuildResult result = Builder.build(files, Helpers.getConfiguration(), true);

        Assertions.assertEquals(3, result.getProjects().size());

        final Optional<Project> projectA = result.getProject("project-a");
        assertTrue(projectA.isPresent());

        final Optional<Project> projectB = result.getProject("project-b");
        assertTrue(projectB.isPresent());

        final Optional<Project> projectC = result.getProject("project-c");
        assertTrue(projectC.isPresent());

        final Set<Project> dependenciesA = projectA.get().getDependencies();
        assertEquals(1, dependenciesA.size());
        assertEquals("project-c", dependenciesA.iterator().next().getName());

        final Set<Project> dependenciesB = projectB.get().getDependencies();
        assertEquals(1, dependenciesB.size());
        assertEquals("project-c", dependenciesB.iterator().next().getName());

        final Set<Project> dependenciesC = projectC.get().getDependencies();
        assertEquals(0, dependenciesC.size());
    }

    @Test
    void testKeywordParameterLinker() {
        final String code =
                "*** Test Cases ***\n" +
                "Test keyword with keyword as argument\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Test keyword with keyword as argument\n" +
                "    Run Keyword If    'two' == 'two'    Log    Execute\n";

        final BuildResult result = Builder.build(code, true);

        final Project project = result.getProjects().iterator().next();

        final Set<UserKeyword> ofInterest = project.findUserKeyword(Token.fromString("Test keyword with keyword as argument"));
        final UserKeyword keyword = ofInterest.iterator().next();

        final KeywordCall call = (KeywordCall)keyword.getStep(0);
        assertEquals(2, call.getArgumentList().size());
        assertEquals(ConditionType.class, call.getArgumentList().get(0).getType().getClass());
        assertEquals(KeywordType.class, call.getArgumentList().get(1).getType().getClass());
    }

    @Test
    void testKeywordParameterLinkerWithVariableCollapsed() {
        final String code =
                "*** Test Cases ***\n" +
                "Test keyword with keyword as argument\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Test keyword with keyword as argument with collapsed arguments\n" +
                "    Run Keyword    @{params}\n" +
                "\n" +
                "*** Variables ***\n" +
                "@{params}    Run Keyword If    'two' == 'two'    Log    Execute";

        final BuildResult result = Builder.build(code, true);

        final Project project = result.getProjects().iterator().next();

        final Set<UserKeyword> ofInterest = project.findUserKeyword(Token.fromString("Test keyword with keyword as argument with collapsed arguments"));
        final UserKeyword keyword = ofInterest.iterator().next();

        final KeywordCall call = (KeywordCall)keyword.getStep(0);
        assertEquals(1, call.getArgumentList().size());
        assertEquals(UnresolvedType.class, call.getArgumentList().get(0).getType().getClass());
    }

    @Test
    void testTooManyArguments(){
        final String code =
                "*** Settings ***\n" +
                "Library    Selenium2Library\n" +
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    Open Browser To Login Page\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Open Browser To Login Page\n" +
                "    Set Selenium Speed    0    Extra argument";

        final BuildResult build = Builder.build(code, true);
        assertFalse(build.getErrors().isEmpty());

        final Set<SyntaxError> syntaxErrors = build.getErrors().inMemory().getSyntaxErrors();
        assertEquals(1, syntaxErrors.size());
        assertTrue(syntaxErrors.iterator().next().getMessage().startsWith(ErrorMessages.TOO_MANY_KEYWORD_ARGUMENTS));
    }
}
