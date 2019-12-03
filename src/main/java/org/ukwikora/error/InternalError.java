package org.ukwikora.error;

public class InternalError extends Error {
    private final Exception exception;

    public InternalError(String message, Exception exception) {
        super(message);
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }
}
