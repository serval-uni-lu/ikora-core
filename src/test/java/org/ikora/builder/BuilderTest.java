package org.ikora.builder;

import org.junit.jupiter.api.Test;
import org.ikora.Helpers;
import org.ikora.model.*;
import org.ikora.utils.FileUtils;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BuilderTest {
    @Test
    void checkParseLibraryVariable(){
        final Project project = Helpers.compileProject("robot/library-variable.robot", true);

        assertEquals(1, project.getTestCases().size());

        final Optional<SourceFile> sourceFile = project.getSourceFile("library-variable.robot");
        assertTrue(sourceFile.isPresent());

        final TestCase testCase = sourceFile.get().getTestCases().get(0);
        assertEquals(1, testCase.getSteps().size());

        final KeywordCall step = (KeywordCall)testCase.getSteps().get(0);
        assertEquals(1, step.getArgumentList().size());

        final Argument argument = step.getArgumentList().get(0);
        assertTrue(argument.getName().contains("Test Status"));
    }

    @Test
    void checkBuildWithValueForVariableContainingDot(){
        final File robot = Helpers.getResourceFile("robot/keyword-with-dot.robot");
        assertNotNull(robot);

        final BuildResult result = Builder.build(robot, Helpers.getConfiguration(), true);
        assertEquals(1, result.getProjects().size());

        final Project project = result.getProjects().iterator().next();

        final Set<UserKeyword> keywords = project.findUserKeyword("Show the content of ${value}");
        assertEquals(1, keywords.size());

        final Optional<SourceFile> sourceFile = project.getSourceFile("keyword-with-dot.robot");
        assertTrue(sourceFile.isPresent());

        final TestCase testCase = sourceFile.get().getTestCases().get(0);
        assertEquals(1, testCase.getSteps().size());
    }

    @Test
    void checkScopedByPrefixResolution(){
        final File robot = Helpers.getResourceFile("robot/scope-testing");
        assertNotNull(robot);

        final BuildResult result = Builder.build(robot, Helpers.getConfiguration(), true);
        assertEquals(1, result.getProjects().size());

        final Project project = result.getProjects().iterator().next();

        Set<UserKeyword> fromResources1 = project.findUserKeyword("Load keyword from resource1");
        assertEquals(1, fromResources1.size());

        Optional<Keyword> resources1Step0 = ((KeywordCall)fromResources1.iterator().next().getStep(0)).getKeyword();
        assertTrue(resources1Step0.isPresent());
        assertEquals("resources1", resources1Step0.get().getLibraryName());

        Set<UserKeyword> fromResources2 = project.findUserKeyword("Load keyword from resource2");
        assertEquals(1, fromResources2.size());

        Optional<Keyword> resources2Step0 = ((KeywordCall)fromResources2.iterator().next().getStep(0)).getKeyword();
        assertTrue(resources2Step0.isPresent());
        assertEquals("resources2", resources2Step0.get().getLibraryName());
    }

    @Test
    void checkDuplicatedStaticallyImportedKeywordsAreDetectedTwice(){
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
    void checkAssignmentFromKeyword(){
        final File robot = Helpers.getResourceFile("robot/assignment/keyword.robot");
        assertNotNull(robot);

        final BuildResult result = Builder.build(robot, Helpers.getConfiguration(), true);
        assertEquals(1, result.getProjects().size());

        final Project project = result.getProjects().iterator().next();

        Set<UserKeyword> userKeywords = project.getUserKeywords();
        assertEquals(3, userKeywords.size());

        Set<UserKeyword> ofInterest = project.findUserKeyword("Test with a simple test case to see how assignment works");
        assertEquals(1, ofInterest.size());

        UserKeyword keyword = ofInterest.iterator().next();
        Step step0 = keyword.getStep(0);

        assertTrue(Assignment.class.isAssignableFrom(step0.getClass()));
        Assignment assignment = (Assignment) step0;

        assertEquals(1, assignment.getReturnVariables().size());
        assertEquals("${EtatRun}", assignment.getReturnVariables().get(0).getName());
        assertEquals(1, assignment.getReturnVariables().get(0).getDependencies().size());
    }

    @Test
    void checkAssignmentFromVariableWithEqualSign(){
        final File robot = Helpers.getResourceFile("robot/assignment/variable.robot");
        assertNotNull(robot);

        final BuildResult result = Builder.build(robot, Helpers.getConfiguration(),true);
        assertEquals(1, result.getProjects().size());

        final Project project = result.getProjects().iterator().next();

        Set<Variable> variables = project.getVariables();
        assertEquals(1, variables.size());
    }

    @Test
    void checkTestCaseSetupWithCall() {
        final File robot = Helpers.getResourceFile("robot/setup-and-teardown.robot");
        assertNotNull(robot);

        final BuildResult result = Builder.build(robot, Helpers.getConfiguration(), true);
        assertEquals(1, result.getProjects().size());

        final Project project = result.getProjects().iterator().next();

        Set<UserKeyword> ofInterest = project.findUserKeyword("Setup the test case");
        assertEquals(1, ofInterest.size());
        Keyword keyword = ofInterest.iterator().next();

        TestCase testCase = project.getTestCases().iterator().next();
        assertTrue(testCase.getSetup().getKeyword().isPresent());
        Keyword setup = testCase.getSetup().getKeyword().get();

        assertEquals(setup, keyword);
    }

    @Test
    void checkTestCaseTeardownWithCall() {
        final File robot = Helpers.getResourceFile("robot/setup-and-teardown.robot");
        assertNotNull(robot);

        final BuildResult result = Builder.build(robot, Helpers.getConfiguration(), true);
        assertEquals(1, result.getProjects().size());

        final Project project = result.getProjects().iterator().next();

        Set<UserKeyword> ofInterest = project.findUserKeyword("Clean the environment");
        assertEquals(1, ofInterest.size());
        Keyword keyword = ofInterest.iterator().next();

        TestCase testCase = project.getTestCases().iterator().next();
        assertTrue(testCase.getTearDown().getKeyword().isPresent());
        Keyword setup = testCase.getTearDown().getKeyword().get();

        assertEquals(setup, keyword);
    }
}