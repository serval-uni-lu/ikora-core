package org.ukwikora.compiler;

import org.junit.jupiter.api.Test;
import org.ukwikora.Globals;
import org.ukwikora.model.*;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CompilerTest {
    @Test
    public void checkParseLibraryVariable(){
        final Project project = Globals.compileProject("robot/library-variable.robot");

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
    public void checkScopedByPrefixResolution(){
        final File robot = Globals.getResourceFile("robot/scope-testing");
        final Project project = Compiler.compile(robot.getAbsolutePath());

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
    public void checkDuplicatedStaticallyImportedKeywordsAreDetectedTwice(){
        final File robot = Globals.getResourceFile("robot/duplicated-keyword");
        final Project project = Compiler.compile(robot.getAbsolutePath());

        assertNotNull(project);

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
    public void checkAssignmentFromRealLife(){
        final File robot = Globals.getResourceFile("robot/assignment");
        final Project project = Compiler.compile(robot.getAbsolutePath());

        assertNotNull(project);

        Set<UserKeyword> userKeywords = project.getUserKeywords();
        assertEquals(3, userKeywords.size());

        Set<UserKeyword> ofInterest = project.findUserKeyword("Test with a simple test case to see how assignment works");
        assertEquals(1, ofInterest.size());

        Keyword keyword = ofInterest.iterator().next();
        Assignment step0 = (Assignment) keyword.getStep(0);
        assertEquals(1, step0.getReturnValues().size());
        assertEquals("${EtatRun}", step0.getReturnValues().get(0).getName());
        assertEquals(1, step0.getReturnValues().get(0).getDependencies().size());
    }
}