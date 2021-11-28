package lu.uni.serval.ikora.core.builder.resolver;

import lu.uni.serval.ikora.core.builder.CallResolver;
import lu.uni.serval.ikora.core.model.Step;
import lu.uni.serval.ikora.core.model.TestCase;
import lu.uni.serval.ikora.core.model.TestProcessing;
import lu.uni.serval.ikora.core.runner.Runtime;

public class TestCaseResolver {
    private TestCaseResolver() {}

    public static void resolve(TestCase testCase, Runtime runtime) {
        testCase.getSetup().flatMap(TestProcessing::getCall).ifPresent(c -> CallResolver.resolve(c, runtime));

        for (Step step: testCase) {
            testCase.getTemplate().flatMap(TestProcessing::getCall).ifPresent(step::setTemplate);
            step.getKeywordCall().ifPresent(c -> CallResolver.resolve(c, runtime));
        }

        testCase.getTearDown().flatMap(TestProcessing::getCall).ifPresent(c -> CallResolver.resolve(c, runtime));
    }
}
