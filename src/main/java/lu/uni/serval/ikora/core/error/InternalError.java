package lu.uni.serval.ikora.core.error;

import lu.uni.serval.ikora.core.model.Range;

public class InternalError extends LocalError {
    public InternalError(String message, Range range) {
        super(message, range);
    }
}
