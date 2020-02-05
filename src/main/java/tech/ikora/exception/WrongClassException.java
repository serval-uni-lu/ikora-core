package tech.ikora.exception;

public class WrongClassException extends Exception {
    private final Class expected;
    private final Class received;

    public WrongClassException(Class expected, Class received) {
        super("Expected class " + expected.getName() + " received class " + received + " instead");
        this.expected = expected;
        this.received = received;
    }

    public Class getExpected() {
        return expected;
    }

    public Class getReceived() {
        return received;
    }
}
