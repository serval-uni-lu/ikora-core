package lu.uni.serval.robotframework.libraries.builtin.variables;

import lu.uni.serval.robotframework.analytics.Action;
import lu.uni.serval.robotframework.analytics.StatusResults;
import lu.uni.serval.robotframework.model.LibraryVariable;

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
