package org.ikora.error;

import org.ikora.model.Position;

public class SymbolError extends LocalError {
    public SymbolError(String message, Position position) {
        super(message, position);
    }
}
