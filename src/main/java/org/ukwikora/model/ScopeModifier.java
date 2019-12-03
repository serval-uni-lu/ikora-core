package org.ukwikora.model;

import org.ukwikora.error.Error;
import org.ukwikora.runner.Runtime;

import java.util.List;

public interface ScopeModifier {
    void addToScope(Runtime runtime, KeywordCall call, List<Error> errors);
}
