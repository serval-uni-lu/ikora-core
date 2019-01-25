package org.ukwikora.model;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

public interface Keyword extends Statement {
    enum Type{
        User, ControlFlow, Assertion, Action, Log, Error, Synchronisation, Get,  Unknown, Set
    }

    Keyword getStep(int position);
    int getSize();
    int getLevel();
    Value.Type[] getArgumentTypes();
    int getMaxArgument();
    int[] getKeywordsLaunchedPosition();
    Set<Keyword> getDependencies();
    List<TestCase> getTestCases();
    List<String> getSuites();

    int getConnectivity(int distance);

    void addDependency(@Nonnull Keyword keyword);

    void execute(Runtime runtime);

    Type getType();
}
