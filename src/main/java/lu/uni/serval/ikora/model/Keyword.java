package lu.uni.serval.ikora.model;

import lu.uni.serval.ikora.types.BaseTypeList;

public interface Keyword extends Node, Dependable {
    enum Type{
        USER, CONFIGURATION, CONTROL_FLOW, ASSERTION, ACTION, LOG, ERROR, SYNCHRONIZATION, GET, SET, UNKNOWN, NONE
    }

    Type getType();
    NodeList<Value> getReturnValues();
    Tokens getDocumentation();
    BaseTypeList getArgumentTypes();
}
