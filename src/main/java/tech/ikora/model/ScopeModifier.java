package tech.ikora.model;

import tech.ikora.runner.Runtime;

public interface ScopeModifier {
    void addToScope(Runtime runtime, KeywordCall call);
}
