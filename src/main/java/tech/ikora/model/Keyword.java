package tech.ikora.model;

import tech.ikora.types.BaseTypeList;

public interface Keyword extends Node {
    enum Type{
        USER, CONFIGURATION, CONTROL_FLOW, ASSERTION, ACTION, LOG, ERROR, SYNCHRONIZATION, GET, SET, UNKNOWN, NONE
    }

    Type getType();
    NodeList<Value> getReturnValues();
    Tokens getDocumentation();
    BaseTypeList getArgumentTypes();
}
