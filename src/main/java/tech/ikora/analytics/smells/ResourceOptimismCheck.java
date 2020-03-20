package tech.ikora.analytics.smells;

import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.analytics.visitor.ResourceOptimismVisitor;
import tech.ikora.model.TestCase;

public class ResourceOptimismCheck implements SmellCheck {
    @Override
    public SmellMetric computeMetric(TestCase testCase) {
        ResourceOptimismVisitor visitor = new ResourceOptimismVisitor();
        visitor.visit(testCase, new PathMemory());

        return new SmellMetric(SmellMetric.Type.RESOURCE_OPTIMISM, visitor.getNumberSleepCalls());
    }
}
