package lu.uni.serval.robotframework.model;

import lu.uni.serval.robotframework.runner.Runtime;

import java.util.Set;

public interface Keyword extends Element {
    enum Type{
        User, ControlFlow, Assertion, Action, Log, Error, Synchronisation, Get,  Unknown
    }

    Keyword getStep(int position);
    int getSize();
    int getDepth();
    Argument.Type[] getArgumentTypes();
    int getMaxArgument();
    int[] getKeywordsLaunchedPosition();
    Set<Keyword> getDependencies();
    int getConnectivity(int distance);

    void addDependency(Keyword keyword);

    void execute(Runtime runtime);

    Type getType();
}
