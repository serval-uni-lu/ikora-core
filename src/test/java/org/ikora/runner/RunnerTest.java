package org.ikora.runner;

import org.ikora.utils.Globals;
import org.junit.jupiter.api.Test;
import org.ikora.Helpers;
import org.ikora.model.Project;
import org.ikora.report.Report;

import static org.junit.jupiter.api.Assertions.*;

class RunnerTest {
    @Test
    void ExecuteSimpleTestSuite(){
        Project project = Helpers.compileProject("robot/library-variable.robot", false);
        Runner runner = new Runner(project);

        try {
            Report report = runner.execute();
            assertEquals(Globals.APPLICATION_CANONICAL, report.getGenerator());
        } catch (UnsupportedOperationException e) {
            e.getMessage();
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
            assertEquals(Globals.APPLICATION_CANONICAL, report.getGenerator());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}