package tech.ikora.error;

import tech.ikora.model.Range;

public class InternalError extends LocalError {
    public InternalError(String message, Range range) {
        super(message, range);
    }
}
