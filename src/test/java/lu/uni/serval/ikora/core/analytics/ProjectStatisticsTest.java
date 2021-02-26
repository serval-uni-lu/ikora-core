package lu.uni.serval.ikora.core.analytics;

import lu.uni.serval.ikora.core.builder.BuildResult;
import lu.uni.serval.ikora.core.builder.Builder;
import lu.uni.serval.ikora.core.exception.InvalidTypeException;
import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.model.TestCase;
import lu.uni.serval.ikora.core.model.UserKeyword;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import lu.uni.serval.ikora.core.Helpers;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProjectStatisticsTest {
    private static ProjectStatistics statistics;

    @BeforeAll
    static void setup(){
        Project project = Helpers.compileProject("robot/web-demo", true);
        statistics = new ProjectStatistics(project);
    }

    @Test
    void testSizeDistributionWithSimpleProject() throws InvalidTypeException {
        Map<Integer, Integer> userKeywordSizes = statistics.getSizeDistribution(UserKeyword.class);
        assertFalse(userKeywordSizes.isEmpty());

        int size2 = userKeywordSizes.getOrDefault(2, 0);
        assertEquals(4, size2);

        int size3 = userKeywordSizes.getOrDefault(3, 0);
        assertEquals(1, size3);

        int size4 = userKeywordSizes.getOrDefault(4, 0);
        assertEquals(1, size4);

        int size6 = userKeywordSizes.getOrDefault(6, 0);
        assertEquals(1, size6);

        int size7 = userKeywordSizes.getOrDefault(7, 0);
        assertEquals(2, size7);
    }

    @Test
    void testSimpleLibraryCallConnectivity() throws InvalidTypeException {
        final String code =
                "*** Test Cases ***\n" +
                "Simple Test\n" +
                "    Print    Something interesting\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Print\n" +
                "    [Arguments]    ${message}\n" +
                "    Log    ${message}\n";

        final BuildResult result = Builder.build(code, true);
        Assertions.assertEquals(1, result.getProjects().size());

        final Project project = result.getProjects().iterator().next();
        final ProjectStatistics projectStatistics = new ProjectStatistics(project);
        final Map<Integer, Integer> userKeywordConnectivity = projectStatistics.getConnectivityDistribution(UserKeyword.class);
        assertEquals(1, userKeywordConnectivity.size());

        int connectivity1 = userKeywordConnectivity.getOrDefault(1,0);
        assertEquals(1, connectivity1);
    }

    @Test
    void testConnectivityDistributionWithWebDemoProject() throws InvalidTypeException {
        Map<Integer, Integer> userKeywordConnectivity = statistics.getConnectivityDistribution(UserKeyword.class);
        assertFalse(userKeywordConnectivity.isEmpty());

        int connectivity0 = userKeywordConnectivity.getOrDefault(0,0);
        assertEquals(1, connectivity0);

        int connectivity1 = userKeywordConnectivity.getOrDefault(1,0);
        assertEquals(3, connectivity1);

        int connectivity2 = userKeywordConnectivity.getOrDefault(2,0);
        assertEquals(4, connectivity2);

        int connectivity3 = userKeywordConnectivity.getOrDefault(3,0);
        assertEquals(1, connectivity3);

        Map<Integer, Integer> testCaseConnectivity = statistics.getConnectivityDistribution(TestCase.class);
        assertFalse(testCaseConnectivity.isEmpty());

        connectivity0 = testCaseConnectivity.getOrDefault(0,0);
        assertEquals(1, connectivity0);
    }

    @Test
    void testLevelDistributionWithWebDemoProject() throws InvalidTypeException {
        Map<Integer, Integer> userKeywordLevels = statistics.getLevelDistribution(UserKeyword.class);
        assertFalse(userKeywordLevels.isEmpty());

        int level1 = userKeywordLevels.getOrDefault(1,0);
        assertEquals(5, level1);

        int level2 = userKeywordLevels.getOrDefault(2,0);
        assertEquals(3, level2);

        int level3 = userKeywordLevels.getOrDefault(3,0);
        assertEquals(1, level3);

        Map<Integer, Integer> testCaseLevel = statistics.getLevelDistribution(TestCase.class);
        assertFalse(userKeywordLevels.isEmpty());

        int level4 = testCaseLevel.getOrDefault(4,0);
        assertEquals(1, level4);
    }

    @Test
    void testSequenceDistributionWebDemoProject() throws InvalidTypeException {
        Map<Integer, Integer> userKeywordSequence = statistics.getSequenceDistribution(UserKeyword.class);
        assertFalse(userKeywordSequence.isEmpty());

        int sequence1 = userKeywordSequence.getOrDefault(1,0);
        assertEquals(4, sequence1);

        int sequence2 = userKeywordSequence.getOrDefault(2,0);
        assertEquals(2, sequence2);

        int sequence3 = userKeywordSequence.getOrDefault(3,0);
        assertEquals(1, sequence3);

        int sequence4 = userKeywordSequence.getOrDefault(4,0);
        assertEquals(2, sequence4);
    }
}