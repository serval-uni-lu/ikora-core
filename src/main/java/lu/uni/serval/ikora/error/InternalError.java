package lu.uni.serval.ikora.error;

import lu.uni.serval.ikora.model.Range;

public class InternalError extends LocalError {
    public InternalError(String message, Range range) {
        super(message, range);
    }
}
