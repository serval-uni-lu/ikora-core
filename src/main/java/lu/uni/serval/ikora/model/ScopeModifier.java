package lu.uni.serval.ikora.model;

import lu.uni.serval.ikora.runner.Runtime;

public interface ScopeModifier {
    void addToScope(Runtime runtime, KeywordCall call);
}
