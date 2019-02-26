package org.ukwikora.analytics;

import org.junit.Before;
import org.junit.Test;
import org.ukwikora.Globals;
import org.ukwikora.model.Project;
import org.ukwikora.model.UserKeyword;

import java.util.Map;

import static org.junit.Assert.*;

public class ProjectStatisticsTest {
    private Project project;
    private ProjectStatistics statistics;

    @Before
    public void setup(){
        project = Globals.compileProject("robot/web-demo");
        statistics = new ProjectStatistics(project);
    }

    @Test
    public void checkSizeDistributionWithSimpleProject(){
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
    public void checkConnectivityDistributionWithSimpleProject(){
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
    }

    @Test
    public void checkLevelDistributionWithSimpleProject(){
        Map<Integer, Integer> userKeywordLevels = statistics.getLevelDistribution(UserKeyword.class);
        assertFalse(userKeywordLevels.isEmpty());
    }
}