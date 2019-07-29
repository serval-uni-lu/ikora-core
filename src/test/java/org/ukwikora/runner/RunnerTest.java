package org.ukwikora.runner;

import org.junit.jupiter.api.Test;
import org.ukwikora.Helpers;
import org.ukwikora.model.Project;
import org.ukwikora.report.Report;
import org.ukwikora.utils.Globals;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class RunnerTest {
    @Test
    void ExecuteSimpleTestSuite(){
        Project project = Helpers.compileProject("robot/library-variable.robot", false);
        Runner runner = new Runner(project);

        try {
            Report report = runner.execute();
            assertEquals(Globals.applicationCanonical, report.getGenerator());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void ExecuteWithSuiteWithAssignment(){
        Project project = Helpers.compileProject("robot/assignment", false);
        Runner runner = new Runner(project);

        try {
            Report report = runner.execute();
            assertEquals(Globals.applicationCanonical, report.getGenerator());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}