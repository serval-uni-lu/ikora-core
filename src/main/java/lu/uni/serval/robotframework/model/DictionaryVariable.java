package lu.uni.serval.robotframework.model;

import lu.uni.serval.analytics.Action;
import lu.uni.serval.utils.Differentiable;

import java.util.List;

public class DictionaryVariable extends Variable {
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
