package lu.uni.serval.ikora.core.model;

import lu.uni.serval.ikora.core.types.BaseTypeList;

public interface Keyword extends Node, Dependable {
    enum Type{
        USER, CONFIGURATION, CONTROL_FLOW, ASSERTION, ACTION, LOG, ERROR, SYNCHRONIZATION, GET, SET, UNKNOWN, NONE
    }

    Type getType();
    NodeList<Value> getReturnValues();
    Tokens getDocumentation();
    BaseTypeList getArgumentTypes();
}
