package lu.uni.serval.ikora.core.builder;

import lu.uni.serval.ikora.core.model.Step;
import lu.uni.serval.ikora.core.runner.Runtime;

public class StepResolver {
    private StepResolver() {}

    public static void resolve(Step step, Runtime runtime){
        step.getKeywordCall().ifPresent(c -> CallResolver.resolve(c, runtime));

        for (Step childStep: step.getSteps()) {
            resolve(childStep, runtime);
        }
    }
}
