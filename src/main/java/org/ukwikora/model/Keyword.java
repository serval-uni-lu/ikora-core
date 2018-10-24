package org.ukwikora.model;

import java.util.List;
import java.util.Set;

public interface Keyword extends Element {
    enum Type{
        User, ControlFlow, Assertion, Action, Log, Error, Synchronisation, Get,  Unknown
    }

    Keyword getStep(int position);
    int getSize();
    int getDepth();
    Value.Type[] getArgumentTypes();
    int getMaxArgument();
    int[] getKeywordsLaunchedPosition();
    Set<Keyword> getDependencies();
    List<TestCase> getTestCases();
    int getConnectivity(int distance);

    void addDependency(Keyword keyword);

    void execute(Runtime runtime);

    Type getType();
}
