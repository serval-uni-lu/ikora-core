package lu.uni.serval.ikora.analytics;

import lu.uni.serval.ikora.model.Project;
import lu.uni.serval.ikora.model.TestCase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import lu.uni.serval.ikora.Helpers;

import static org.junit.jupiter.api.Assertions.*;

class KeywordStatisticsTest {
    static Project project;

    @BeforeAll
    static void setup(){
        project = Helpers.compileProject("robot/web-demo", true);
    }

    @Test
    void testNumberOfSteps_validTestCase(){
        TestCase testCase = project.findTestCase(null, "Valid Login").iterator().next();
        final int numberSteps = KeywordStatistics.getStatementCount(testCase);
        assertEquals(17, numberSteps);
    }
}