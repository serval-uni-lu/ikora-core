package org.ukwikora.model;

import org.ukwikora.analytics.Action;
import org.ukwikora.analytics.StatusResults;

import java.util.List;

public class ListVariable extends Variable {
    @Override
    public double distance(StatusResults.Differentiable other) {
        return 0;
    }

    @Override
    public List<Action> differences(StatusResults.Differentiable other) {
        return null;
    }

    @Override
    public void addElement(String element) {

    }

    @Override
    public String getValueAsString() {
        return null;
    }
}
