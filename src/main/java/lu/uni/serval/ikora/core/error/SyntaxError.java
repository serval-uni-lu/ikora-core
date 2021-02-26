package lu.uni.serval.ikora.core.error;

import lu.uni.serval.ikora.core.model.Range;

public class SyntaxError extends LocalError {
    public SyntaxError(String message, Range range) {
        super(message, range);
    }
}
