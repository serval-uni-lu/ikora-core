package tech.ikora.error;

import tech.ikora.model.Range;

public class SymbolError extends LocalError {
    public SymbolError(String message, Range range) {
        super(message, range);
    }
}
