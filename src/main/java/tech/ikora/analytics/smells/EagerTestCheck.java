package tech.ikora.analytics.smells;

import org.apache.commons.lang3.NotImplementedException;
import tech.ikora.model.TestCase;

class EagerTestCheck implements SmellCheck{
    @Override
    public SmellMetric computeMetric(TestCase testCase) {
        throw new NotImplementedException("EagerTestCheck not implemented yet");
    }
}
