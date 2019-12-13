package org.ikora.model;

import java.util.List;

public abstract class Keyword extends Node {
    public enum Type{
        User, ControlFlow, Assertion, Action, Log, Error, Synchronisation, Get,  Unknown, Set
    }

    public abstract Type getType();
    public abstract List<Value> getReturnValues();
    public abstract String getDocumentation();
    public abstract int getMaxNumberArguments();
}
