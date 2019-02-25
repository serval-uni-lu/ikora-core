package org.ukwikora.model;

public interface Keyword extends Statement {
    enum Type{
        User, ControlFlow, Assertion, Action, Log, Error, Synchronisation, Get,  Unknown, Set
    }

    Keyword getStep(int position);

    Value.Type[] getArgumentTypes();
    int getMaxArgument();
    int[] getKeywordsLaunchedPosition();

    void execute(Runtime runtime);

    Type getType();
}
