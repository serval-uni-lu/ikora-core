package lu.uni.serval.ikora.core.analytics;

import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.model.Step;
import lu.uni.serval.ikora.core.model.TestCase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import lu.uni.serval.ikora.core.Helpers;

import static org.junit.jupiter.api.Assertions.*;

class KeywordStatisticsTest {
    static Project project;

    @BeforeAll
    static void setup(){
        project = Helpers.compileProject("robot/web-demo", true);
    }

    @Test
    void testSequenceSizeForTestCase(){
        final TestCase testCase = project.findTestCase(null, "Valid Login").iterator().next();
        int numberSteps = KeywordStatistics.getStatementCount(testCase);

        assertEquals(17, numberSteps);
    }

    @Test
    void testSequenceSizeForStep(){
        final  TestCase testCase = project.findTestCase(null, "Valid Login").iterator().next();
        final Step step = testCase.getStep(0);
        int numberSteps = KeywordStatistics.getSequenceSize(step);

        assertEquals(4, numberSteps);
    }
}