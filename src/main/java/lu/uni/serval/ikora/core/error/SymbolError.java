package lu.uni.serval.ikora.core.error;

import lu.uni.serval.ikora.core.model.Range;

public class SymbolError extends LocalError {
    public SymbolError(String message, Range range) {
        super(message, range);
    }
}
