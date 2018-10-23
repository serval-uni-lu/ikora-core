package org.ukwikora.libraries.builtin.variables;

import org.ukwikora.analytics.Action;
import org.ukwikora.analytics.StatusResults;
import org.ukwikora.model.LibraryVariable;

import java.util.List;

public class Empty extends LibraryVariable {
    @Override
    public double distance(StatusResults.Differentiable other) {
        return 0;
    }

    @Override
    public List<Action> differences(StatusResults.Differentiable other) {
        return null;
    }
}
