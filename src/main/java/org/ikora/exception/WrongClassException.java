package org.ikora.exception;

public class WrongClassException extends Exception {
    public Class expected;
    public Class received;

    public WrongClassException(Class expected, Class received) {
        super("Expected class " + expected.getName() + " received class " + received + " instead");
        this.expected = expected;
        this.received = received;
    }
}
