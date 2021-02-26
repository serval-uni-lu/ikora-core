package lu.uni.serval.ikora.core.error;

public class UnhandledError extends Error {
    private final Exception exception;

    public UnhandledError(String message, Exception exception) {
        super(message);
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }
}
