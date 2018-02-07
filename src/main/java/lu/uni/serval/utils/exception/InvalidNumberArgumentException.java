package lu.uni.serval.utils.exception;

public class InvalidNumberArgumentException extends Exception {
    public int expected;
    public int actual;

    public InvalidNumberArgumentException(int expected, int actual) {
        this.expected = expected;
        this.actual = actual;
    }
}
