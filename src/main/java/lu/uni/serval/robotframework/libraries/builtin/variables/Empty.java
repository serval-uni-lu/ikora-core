package lu.uni.serval.robotframework.libraries.builtin.variables;

import lu.uni.serval.analytics.Action;
import lu.uni.serval.robotframework.model.LibraryVariable;
import lu.uni.serval.utils.Differentiable;

import java.util.List;

public class Empty extends LibraryVariable {
    @Override
    public double distance(Differentiable other) {
        return 0;
    }

    @Override
    public List<Action> differences(Differentiable other) {
        return null;
    }
}
