package org.ukwikora.model;

import org.ukwikora.analytics.Action;
import org.ukwikora.analytics.StatusResults;

import java.util.List;

public class ListVariable extends Variable {
    @Override
    public double distance(Differentiable other) {
        return 0;
    }

    @Override
    public List<Action> differences(Differentiable other) {
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
