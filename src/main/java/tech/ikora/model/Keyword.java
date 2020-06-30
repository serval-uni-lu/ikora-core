package tech.ikora.model;

import tech.ikora.types.BaseTypeList;

public interface Keyword extends Node {
    enum Type{
        USER, CONTROL_FLOW, ASSERTION, ACTION, LOG, ERROR, SYNCHRONISATION, GET, UNKNOWN, SET
    }

    Type getType();
    NodeList<Value> getReturnValues();
    Tokens getDocumentation();
    BaseTypeList getArgumentTypes();
}
