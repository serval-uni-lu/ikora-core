package org.ikora.model;

import org.ikora.error.ErrorManager;
import org.ikora.runner.Runtime;

public interface ScopeModifier {
    void addToScope(Runtime runtime, KeywordCall call, ErrorManager errors);
}
