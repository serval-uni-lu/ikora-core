package org.ukwikora.model;

import org.ukwikora.runner.Runtime;

import java.util.List;

public interface Keyword extends Node {
    enum Type{
        User, ControlFlow, Assertion, Action, Log, Error, Synchronisation, Get,  Unknown, Set
    }

    Keyword getStep(int position);

    Value.Type[] getArgumentTypes();
    int getMaxArgument();
    int[] getKeywordsLaunchedPosition();

    void execute(Runtime runtime) throws Exception;

    Type getType();
    String getDocumentation();

    List<Value> getReturnValues();
}
