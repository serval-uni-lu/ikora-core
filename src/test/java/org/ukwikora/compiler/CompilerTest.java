package org.ukwikora.compiler;

import org.junit.Test;
import org.ukwikora.Globals;
import org.ukwikora.model.*;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class CompilerTest {
    @Test
    public void checkParseLibraryVariable(){
        final File robot = Globals.getResourceFile("robot/library-variable.robot");
        final Project project = Compiler.compile(robot.getAbsolutePath());

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
}