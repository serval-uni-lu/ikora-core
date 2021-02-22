package lu.uni.serval.ikora.error;

import lu.uni.serval.ikora.model.Range;

public class SymbolError extends LocalError {
    public SymbolError(String message, Range range) {
        super(message, range);
    }
}
