package tech.ikora.analytics.smells;

import tech.ikora.model.TestCase;

public interface SmellCheck {
    SmellMetric computeMetric(TestCase testCase);
}
