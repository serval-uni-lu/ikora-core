package tech.ikora.analytics.smells;

import tech.ikora.analytics.visitor.HardCodedValuesVisitor;
import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.TestCase;

public class HardcodedValuesCheck implements SmellCheck {
    @Override
    public SmellMetric computeMetric(TestCase testCase) {
        HardCodedValuesVisitor visitor = new HardCodedValuesVisitor();
        visitor.visit(testCase, new PathMemory());

        return new SmellMetric(SmellMetric.Type.HARD_CODED_VALUES, visitor.getNumberHardcodedValues());
    }
}
