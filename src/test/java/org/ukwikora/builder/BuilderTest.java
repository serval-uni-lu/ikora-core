package org.ukwikora.builder;

import org.junit.jupiter.api.Test;
import org.ukwikora.Helpers;
import org.ukwikora.model.*;
import org.ukwikora.utils.FileUtils;

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

        final TestCaseFile testCaseFile = project.getTestCaseFile("library-variable.robot");
        assertNotNull(testCaseFile);

        final TestCase testCase = testCaseFile.getTestCases().get(0);
        assertEquals(1, testCase.getSteps().size());

        final KeywordCall step = (KeywordCall)testCase.getSteps().get(0);
        assertEquals(1, step.getParameters().size());

        final Value value = step.getParameters().get(0);
        final Optional<List<Value>> resolvedValues = value.getResolvedValues();

        assertTrue(resolvedValues.isPresent());
        assertEquals(1, resolvedValues.get().size());
    }

    @Test
    void checkBuildWithLongKeyword(){
        final File robot = Helpers.getResourceFile("robot/long-keyword.robot");
        assertNotNull(robot);

        final Project project = Builder.build(robot, true);
        assertNotNull(project);

        final Set<UserKeyword> keywords = project.findUserKeyword("Annuler un ordre permanent de <${Montant_virement}> : créditeur <${Nom_créditeur}> N°compte <${Numero_compte_créditeur}> - débiteur <${Nom_débiteur}> N°compte <${Numero_compte_débiteur}>");
        assertEquals(1, keywords.size());

        final TestCaseFile testCaseFile = project.getTestCaseFile("library-variable.robot");
        assertNotNull(testCaseFile);

        final TestCase testCase = testCaseFile.getTestCases().get(0);
        assertEquals(1, testCase.getSteps().size());
    }

    @Test
    void checkScopedByPrefixResolution(){
        final File robot = Helpers.getResourceFile("robot/scope-testing");
        assertNotNull(robot);

        final Project project = Builder.build(robot, true);
        assertNotNull(project);

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
        final Project project = Builder.build(robot, true);

        assertNotNull(project);

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
    void checkAssignmentFromRealLife(){
        final File robot = Helpers.getResourceFile("robot/assignment");
        assertNotNull(robot);

        final Project project = Builder.build(robot, true);
        assertNotNull(project);

        Set<UserKeyword> userKeywords = project.getUserKeywords();
        assertEquals(3, userKeywords.size());

        Set<UserKeyword> ofInterest = project.findUserKeyword("Test with a simple test case to see how assignment works");
        assertEquals(1, ofInterest.size());

        Keyword keyword = ofInterest.iterator().next();
        Assignment step0 = (Assignment) keyword.getStep(0);
        assertEquals(1, step0.getReturnVariables().size());
        assertEquals("${EtatRun}", step0.getReturnVariables().get(0).getName());
        assertEquals(1, step0.getReturnVariables().get(0).getDependencies().size());
    }

    @Test
    void checkTestCaseSetupWithCall() {
        final File robot = Helpers.getResourceFile("robot/setup-and-teardown.robot");
        assertNotNull(robot);

        final Project project = Builder.build(robot, true);
        assertNotNull(project);

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

        final Project project = Builder.build(robot, true);
        assertNotNull(project);

        Set<UserKeyword> ofInterest = project.findUserKeyword("Clean the environment");
        assertEquals(1, ofInterest.size());
        Keyword keyword = ofInterest.iterator().next();

        TestCase testCase = project.getTestCases().iterator().next();
        assertTrue(testCase.getTearDown().getKeyword().isPresent());
        Keyword setup = testCase.getTearDown().getKeyword().get();

        assertEquals(setup, keyword);
    }
}