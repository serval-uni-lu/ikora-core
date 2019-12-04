package org.ukwikora.model;

import org.ukwikora.error.ErrorManager;
import org.ukwikora.runner.Runtime;

public interface ScopeModifier {
    void addToScope(Runtime runtime, KeywordCall call, ErrorManager errors);
}
