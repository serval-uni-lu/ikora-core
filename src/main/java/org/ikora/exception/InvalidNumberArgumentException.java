package org.ikora.exception;

public class InvalidNumberArgumentException extends Exception {
    private final int expected;
    private final int received;

    public InvalidNumberArgumentException(int expected, int received) {
        this.expected = expected;
        this.received = received;
    }

    public int getExpected() {
        return expected;
    }

    public int getReceived() {
        return received;
    }
}
