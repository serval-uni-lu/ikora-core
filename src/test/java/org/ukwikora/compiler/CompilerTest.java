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

        //Keyword resources1Step0 = ((KeywordCall)fromResources1.get().getStep(0)).getKeyword();
        //assertEquals("resources1", resources1Step0.getLibraryName());

        Set<UserKeyword> fromResources2 = project.findUserKeyword("Load keyword from resource2");
        assertEquals(1, fromResources2.size());
        //Keyword resources2Step0 = ((KeywordCall)fromResources2.get().getStep(0)).getKeyword();
        //assertEquals("resources2", resources2Step0.getLibraryName());
    }
}