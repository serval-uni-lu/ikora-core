package lu.uni.serval.ikora.error;

import lu.uni.serval.ikora.model.Range;

public class SyntaxError extends LocalError {
    public SyntaxError(String message, Range range) {
        super(message, range);
    }
}
