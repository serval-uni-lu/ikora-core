package lu.uni.serval.robotframework.exception;

public class InvalidNumberArgumentException extends Exception {
    public int expected;
    public int received;

    public InvalidNumberArgumentException(int expected, int received) {
        this.expected = expected;
        this.received = received;
    }
}
