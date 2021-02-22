package lu.uni.serval.ikora.exception;

public class InvalidNumberArgumentException extends InvalidArgumentException {
    private final int expected;
    private final int received;

    public InvalidNumberArgumentException(int expected, int received) {
        super(String.format("Expected %d arguments, got %d instead", expected, received));

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
