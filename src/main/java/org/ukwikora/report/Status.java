package org.ukwikora.report;

public class Status {
    public enum Type{
        PASSED, FAILED, NOT_EXECUTED
    }

    private Type type;

    public Status(){
        this.type = Type.NOT_EXECUTED;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
