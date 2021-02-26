package lu.uni.serval.ikora.core.model;

import lu.uni.serval.ikora.core.runner.Runtime;

public interface ScopeModifier {
    void addToScope(Runtime runtime, KeywordCall call);
}
