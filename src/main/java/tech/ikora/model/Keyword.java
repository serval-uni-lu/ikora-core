package tech.ikora.model;

import tech.ikora.types.BaseTypeList;

import java.util.List;

public interface Keyword extends Node {
    enum Type{
        USER, CONTROL_FLOW, ASSERTION, ACTION, LOG, ERROR, SYNCHRONISATION, GET, UNKNOWN, SET
    }

    Type getType();
    ArgumentList getReturnValues();
    Tokens getDocumentation();
    BaseTypeList getArgumentTypes();
}
