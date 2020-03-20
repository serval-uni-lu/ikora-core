package tech.ikora.analytics.smells;

import tech.ikora.model.TestCase;

import java.util.*;

public class SmellDetector {
    private final static Map<SmellMetric.Type, SmellCheck> smellChecks;
    private final Set<SmellMetric.Type> smellsToDetect;

    static {
        smellChecks = new HashMap<>();
        smellChecks.put(SmellMetric.Type.EAGER_TEST, new EagerTestCheck());
        smellChecks.put(SmellMetric.Type.RESOURCE_OPTIMISM, new ResourceOptimismCheck());
        smellChecks.put(SmellMetric.Type.HARD_CODED_VALUES, new HardcodedValuesCheck());
    }

    public SmellDetector(Set<SmellMetric.Type> smellsToDetect){
        this.smellsToDetect = smellsToDetect;
    }

    public Set<SmellMetric> computeMetrics(TestCase testCase){
       Set<SmellMetric> metrics = new HashSet<>();

       for(SmellMetric.Type type: smellsToDetect){
            metrics.add(smellChecks.get(type).computeMetric(testCase));
       }

        return metrics;
    }
}
