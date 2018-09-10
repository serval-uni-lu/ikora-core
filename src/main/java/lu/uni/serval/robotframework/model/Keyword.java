package lu.uni.serval.robotframework.model;

import lu.uni.serval.robotframework.runner.Runtime;
import lu.uni.serval.utils.Differentiable;

import java.util.List;
import java.util.Set;

public interface Keyword extends Differentiable {
    Keyword getStep(int position);
    Argument getName();

    void addDependency(Keyword keyword);

    void execute(Runtime runtime);

    int getSize();
    List<Keyword> getSequence();
    Set<Keyword> getDependencies();
}
