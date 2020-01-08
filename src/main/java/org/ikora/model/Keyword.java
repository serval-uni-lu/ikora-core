package org.ikora.model;

import java.util.List;

public abstract class Keyword extends Node {
    public enum Type{
        USER, CONTROL_FLOW, ASSERTION, ACTION, LOG, ERROR, SYNCHRONISATION, GET, UNKNOWN, SET
    }

    public abstract Type getType();
    public abstract List<Value> getReturnValues();
    public abstract String getDocumentation();
    public abstract int getMaxNumberArguments();
}
