package lu.uni.serval.ikora.core.runner.executors;

import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.runner.Helpers;
import lu.uni.serval.ikora.core.runner.Runner;
import lu.uni.serval.ikora.core.runner.report.MessageNode;
import lu.uni.serval.ikora.core.runner.report.Report;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentExecutorTest {
    @ParameterizedTest
    @CsvSource(value = {
            "literal:Test 1",
            "variable:Test 1",
            "composed-variable:Test 1-Test",
            "named:Test 1"},
            delimiter = ':'
    )
    void testLiteral(String source, String expected) throws Exception {
        final Project project = Helpers.compileProject("projects/runner/arguments/" + source + ".ikora");
        final Runner runner = new Runner(project);
        final Report report = runner.execute();

        final MessageNode message = report.getSuites().get(0).getTests().get(0).getKeywords().get(0).getMessage();
        assertEquals("INFO", message.getLevel());
        assertEquals(expected, message.getText());
    }

}