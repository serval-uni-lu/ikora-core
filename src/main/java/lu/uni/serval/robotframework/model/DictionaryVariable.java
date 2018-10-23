package lu.uni.serval.robotframework.model;

import lu.uni.serval.robotframework.analytics.Action;
import lu.uni.serval.robotframework.analytics.StatusResults;

import java.util.List;

public class DictionaryVariable extends Variable {
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
