package tech.ikora.runner;

import tech.ikora.utils.Globals;
import org.junit.jupiter.api.Test;
import tech.ikora.Helpers;
import tech.ikora.model.Project;
import tech.ikora.report.Report;

import static org.junit.jupiter.api.Assertions.*;

class RunnerTest {
    @Test
    void ExecuteSimpleTestSuite(){
        Project project = Helpers.compileProject("robot/library-variable.robot", false);
        Runner runner = new Runner(project);

        try {
            Report report = runner.execute();
            assertEquals(Globals.APPLICATION_CANONICAL, report.getGenerator());
            fail("Should have raised an UnsupportedOperationException at this point.");
        } catch (UnsupportedOperationException e) {
            e.getMessage();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void ExecuteWithSuiteWithAssignment() {
        Project project = Helpers.compileProject("robot/assignment", false);
        Runner runner = new Runner(project);

        try{
            Report report = runner.execute();
            assertEquals(Globals.APPLICATION_CANONICAL, report.getGenerator());
            fail("Should have raised an UnsupportedOperationException at this point.");
        } catch (UnsupportedOperationException e) {
            e.getMessage();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}