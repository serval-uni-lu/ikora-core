package tech.ikora.model;

import java.util.List;

public interface Keyword extends Node {
    enum Type{
        USER, CONTROL_FLOW, ASSERTION, ACTION, LOG, ERROR, SYNCHRONISATION, GET, UNKNOWN, SET
    }

    Type getType();
    List<Variable> getReturnVariables();
    Tokens getDocumentation();
    int getMaxNumberArguments();
}
