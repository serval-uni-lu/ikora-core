package org.ukwikora.model;

import java.util.List;

public interface Keyword extends Statement {
    enum Type{
        User, ControlFlow, Assertion, Action, Log, Error, Synchronisation, Get,  Unknown, Set
    }

    Keyword getStep(int position);

    Value.Type[] getArgumentTypes();
    int getMaxArgument();
    int[] getKeywordsLaunchedPosition();

    int getSize();
    int getLevel();
    List<String> getSuites();
    int getConnectivity(int distance);

    void execute(Runtime runtime);

    Type getType();
}
