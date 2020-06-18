package tech.ikora.builder;

import org.junit.jupiter.api.Test;
import tech.ikora.Helpers;
import tech.ikora.model.*;
import tech.ikora.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BuilderTest {
    @Test
    void testParseLibraryVariable(){
        final Project project = Helpers.compileProject("robot/library-variable.robot", true);

        assertEquals(1, project.getTestCases().size());

        final Optional<SourceFile> sourceFile = project.getSourceFile("library-variable.robot");
        assertTrue(sourceFile.isPresent());

        final TestCase testCase = sourceFile.get().getTestCases().get(0);
        assertEquals(1, testCase.getSteps().size());
        assertEquals(2, testCase.getRange().getStart().getLine());

        final KeywordCall step = (KeywordCall)testCase.getSteps().get(0);
        assertEquals(1, step.getArgumentList().size());

        final Argument argument = step.getArgumentList().get(0);
        assertTrue(argument.getNameToken().getText().contains("Test Status"));
    }

    @Test
    void testBuildWithValueForVariableContainingDot() throws IOException, URISyntaxException {
        final File robot = FileUtils.getResourceFile("robot/keyword-with-dot.robot");
        assertNotNull(robot);

        final BuildResult result = Builder.build(robot, Helpers.getConfiguration(), true);
        assertEquals(1, result.getProjects().size());

        final Project project = result.getProjects().iterator().next();

        final Set<UserKeyword> keywords = project.findUserKeyword(Token.fromString("Show the content of ${value}"));
        assertEquals(1, keywords.size());

        final Optional<SourceFile> sourceFile = project.getSourceFile("keyword-with-dot.robot");
        assertTrue(sourceFile.isPresent());

        final TestCase testCase = sourceFile.get().getTestCases().get(0);
        assertEquals(1, testCase.getSteps().size());

        final Tokens tokens = sourceFile.get().getTokens();
        assertEquals(8, tokens.size());
    }

    @Test
    void testScopedByPrefixResolution() throws IOException, URISyntaxException {
        final File robot = FileUtils.getResourceFile("robot/scope-testing");
        assertNotNull(robot);

        final BuildResult result = Builder.build(robot, Helpers.getConfiguration(), true);
        assertEquals(1, result.getProjects().size());
        assertTrue(result.getErrors().isEmpty());

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
        assertEquals(1, result.getProjects().size());

        final Project project = result.getProjects().iterator().next();

        List<TestCase> mainTest = project.getTestCases();
        assertEquals(1, mainTest.size());

        KeywordCall simpleCall = (KeywordCall) mainTest.iterator().next().getStep(0);
        assertNotNull(simpleCall);
        assertEquals(1, simpleCall.getAllPotentialKeywords(Link.Import.BOTH).size());

        KeywordCall duplicateCall = (KeywordCall) mainTest.iterator().next().getStep(1);
        assertNotNull(duplicateCall);
        assertEquals(2, duplicateCall.getAllPotentialKeywords(Link.Import.BOTH).size());
    }

    @Test
    void testAssignmentFromKeyword() throws IOException, URISyntaxException {
        final File robot = FileUtils.getResourceFile("robot/assignment/keyword.robot");
        assertNotNull(robot);

        final BuildResult result = Builder.build(robot, Helpers.getConfiguration(), true);
        assertEquals(1, result.getProjects().size());

        final Project project = result.getProjects().iterator().next();

        final Set<UserKeyword> userKeywords = project.getUserKeywords();
        assertEquals(3, userKeywords.size());

        final Set<UserKeyword> ofInterest = project.findUserKeyword(Token.fromString("Test with a simple test case to see how assignment works"));
        assertEquals(1, ofInterest.size());

        final UserKeyword keyword = ofInterest.iterator().next();
        final Step step0 = keyword.getStep(0);

        assertTrue(Assignment.class.isAssignableFrom(step0.getClass()));
        final Assignment assignment = (Assignment) step0;

        assertEquals(1, assignment.getLeftHandOperand().size());
        assertEquals("${EtatRun}", assignment.getLeftHandOperand().get(0).getNameToken().getText());
        assertEquals(0, assignment.getLeftHandOperand().get(0).getDependencies().size());
    }

    @Test
    void testForLoopWithRange() throws IOException, URISyntaxException {
        final File robot = FileUtils.getResourceFile("robot/for-loop/simple.robot");
        assertNotNull(robot);

        final BuildResult result = Builder.build(robot, Helpers.getConfiguration(), true);
        assertEquals(1, result.getProjects().size());

        final Project project = result.getProjects().iterator().next();

        final Set<UserKeyword> userKeywords = project.getUserKeywords();
        assertEquals(1, userKeywords.size());

        final Set<UserKeyword> ofInterest = project.findUserKeyword(Token.fromString("Simple for loop"));
        assertEquals(1, ofInterest.size());

        final UserKeyword keyword = ofInterest.iterator().next();
        final Step step0 = keyword.getStep(0);

        assertTrue(ForLoop.class.isAssignableFrom(step0.getClass()));
        final ForLoop forLoop = (ForLoop) step0;
        assertEquals("${index}", forLoop.getIterator().getNameToken().getText());

        final List<Step> steps = forLoop.getSteps();
        assertEquals(2, steps.size());
    }

    @Test
    void testForLoopWithRangeWithoutTabAfterIn() throws IOException, URISyntaxException {
        final File robot = FileUtils.getResourceFile("robot/for-loop/simple-without-tab-after-in.robot");
        assertNotNull(robot);

        final BuildResult result = Builder.build(robot, Helpers.getConfiguration(), true);
        assertEquals(1, result.getProjects().size());

        final Project project = result.getProjects().iterator().next();

        final Set<UserKeyword> userKeywords = project.getUserKeywords();
        assertEquals(1, userKeywords.size());

        final Set<UserKeyword> ofInterest = project.findUserKeyword(Token.fromString("Simple for loop"));
        assertEquals(1, ofInterest.size());

        final UserKeyword keyword = ofInterest.iterator().next();
        final Step step0 = keyword.getStep(0);

        assertTrue(ForLoop.class.isAssignableFrom(step0.getClass()));
        final ForLoop forLoop = (ForLoop) step0;
        assertEquals("${index}", forLoop.getIterator().getNameToken().getText());

        final List<Step> steps = forLoop.getSteps();
        assertEquals(2, steps.size());
    }

    @Test
    void testAssignmentFromVariableWithEqualSign() throws IOException, URISyntaxException {
        final File robot = FileUtils.getResourceFile("robot/assignment/variable.robot");
        assertNotNull(robot);

        final BuildResult result = Builder.build(robot, Helpers.getConfiguration(),true);
        assertEquals(1, result.getProjects().size());

        final Project project = result.getProjects().iterator().next();

        Set<VariableAssignment> variables = project.getVariables();
        assertEquals(1, variables.size());
    }

    @Test
    void testTestCaseSetupWithCall() throws IOException, URISyntaxException {
        final File robot = FileUtils.getResourceFile("robot/setup-and-teardown.robot");
        assertNotNull(robot);

        final BuildResult result = Builder.build(robot, Helpers.getConfiguration(), true);
        assertEquals(1, result.getProjects().size());

        final Project project = result.getProjects().iterator().next();

        Set<UserKeyword> ofInterest = project.findUserKeyword(Token.fromString("Setup the test case"));
        assertEquals(1, ofInterest.size());
        Keyword keyword = ofInterest.iterator().next();

        TestCase testCase = project.getTestCases().iterator().next();
        assertTrue(testCase.getSetup().getKeyword().isPresent());
        Keyword setup = testCase.getSetup().getKeyword().get();

        assertEquals(setup, keyword);
    }

    @Test
    void testTestCaseTeardownWithCall() throws IOException, URISyntaxException {
        final File robot = FileUtils.getResourceFile("robot/setup-and-teardown.robot");
        assertNotNull(robot);

        final BuildResult result = Builder.build(robot, Helpers.getConfiguration(), true);
        assertEquals(1, result.getProjects().size());

        final Project project = result.getProjects().iterator().next();

        Set<UserKeyword> ofInterest = project.findUserKeyword(Token.fromString("Clean the environment"));
        assertEquals(1, ofInterest.size());
        Keyword keyword = ofInterest.iterator().next();

        TestCase testCase = project.getTestCases().iterator().next();
        assertTrue(testCase.getTearDown().getKeyword().isPresent());
        Keyword setup = testCase.getTearDown().getKeyword().get();

        assertEquals(keyword, setup);
    }

    @Test
    void testTestCaseWithSimpleTemplate() throws IOException, URISyntaxException {
        final File robot = FileUtils.getResourceFile("robot/template.robot");
        assertNotNull(robot);

        final BuildResult result = Builder.build(robot, Helpers.getConfiguration(), true);
        assertEquals(1, result.getProjects().size());

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

        assertEquals(3, result.getProjects().size());

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
    void testKeywordParameterLinker() throws IOException, URISyntaxException {
        final File file = FileUtils.getResourceFile("robot/keyword-parameters.robot");
        final BuildResult result = Builder.build(file, Helpers.getConfiguration(), true);

        final Project project = result.getProjects().iterator().next();

        final Set<UserKeyword> ofInterest = project.findUserKeyword(Token.fromString("Test keyword with keyword as argument"));
        final UserKeyword keyword = ofInterest.iterator().next();

        final KeywordCall call = (KeywordCall)keyword.getStep(0);
        assertEquals(2, call.getArgumentList().size());
        assertTrue(call.getArgumentList().get(1).getDefinition().get() instanceof KeywordCall);
    }
}