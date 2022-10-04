package lu.uni.serval.ikora.core.runner.executors;

import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.runner.Helpers;
import lu.uni.serval.ikora.core.runner.Runner;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;
import lu.uni.serval.ikora.core.runner.report.MessageNode;
import lu.uni.serval.ikora.core.runner.report.Report;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentExecutorTest {
    @ParameterizedTest
    @CsvSource(value = {
            "literal:Test 1:0",
            "variable:Test 1:0",
            "composed-variable:Test 1-Test:0",
            "named:Test 1:0",
            "keyword:Test 1:1",
            "expand-dictionary:Test 1:0"},
            delimiter = ':'
    )
    void testArgumentInTest(String source, String expected, int position) throws Exception {
        final Project project = Helpers.compileProject("projects/runner/arguments/" + source + ".ikora");
        final Runner runner = new Runner(project);
        final Report report = runner.execute();

        final MessageNode message = report.getSuites().get(0).getTests().get(0).getKeywords().get(position).getMessage();
        assertEquals("INFO", message.getLevel());
        assertEquals(expected, message.getText());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "keyword-argument:Test 1:0",
            "test-variable:Test 1:1",
            "test-named:Test 1:1"},
            delimiter = ':'
    )
    void testArgumentInKeyword(String source, String expected, int position) throws RunnerException {
        final Project project = Helpers.compileProject("projects/runner/arguments/" + source + ".ikora");
        final Runner runner = new Runner(project);
        final Report report = runner.execute();

        final MessageNode message = report.getSuites().get(0).getTests().get(0).getKeywords().get(position).getKeywords().get(0).getMessage();
        assertEquals("INFO", message.getLevel());
        assertEquals(expected, message.getText());
    }
}