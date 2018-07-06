package lu.uni.serval.robotframework.model;

import lu.uni.serval.robotframework.runner.Runtime;

import java.util.Set;

public interface Keyword {
    Keyword getStep(int position);
    Set<Keyword> getDependencies();
    Argument getName();

    void addDependency(Keyword keyword);

    void execute(Runtime runtime);
}
