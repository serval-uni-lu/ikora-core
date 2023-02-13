package lu.uni.serval.ikora.core.runner;

import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;
import lu.uni.serval.ikora.core.runner.report.Report;
import lu.uni.serval.ikora.core.runner.report.StatusNode;
import org.junit.jupiter.api.Test;

import static lu.uni.serval.ikora.core.runner.Helpers.compileProject;
import static org.junit.jupiter.api.Assertions.*;

class RunnerTest {
    @Test
    void testSingleFile() throws RunnerException {
        final Project project = compileProject("projects/runner/simple-run");
        final Runner runner = new Runner(project, new ExecutionFilter(), new OutputStrategy());
        final Report report = runner.execute();
        assertEquals(StatusNode.Type.PASSED, report.getStatus().getType());
        assertEquals(1, report.getNumberTests());
    }

    @Test
    void testBuiltInVariable() throws RunnerException {
        final Project project = compileProject("projects/runner/builtin-variable");
        final Runner runner = new Runner(project, new ExecutionFilter(), new OutputStrategy());
        final Report report = runner.execute();
        assertEquals(StatusNode.Type.PASSED, report.getStatus().getType());
        assertEquals(1, report.getNumberTests());
    }
}