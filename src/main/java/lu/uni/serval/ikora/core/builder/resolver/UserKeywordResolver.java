package lu.uni.serval.ikora.core.builder.resolver;

import lu.uni.serval.ikora.core.model.Step;
import lu.uni.serval.ikora.core.model.UserKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;

public class UserKeywordResolver {
    private UserKeywordResolver() {}

    public static void resolve(UserKeyword keyword, Runtime runtime){
        for (Step step: keyword) {
            StepResolver.resolve(step, runtime);
        }
    }
}
