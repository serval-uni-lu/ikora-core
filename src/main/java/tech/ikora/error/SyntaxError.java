package tech.ikora.error;

import tech.ikora.model.Range;

public class SyntaxError extends LocalError {
    public SyntaxError(String message, Range range) {
        super(message, range);
    }
}
