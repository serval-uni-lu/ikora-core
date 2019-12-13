package org.ikora.error;

import org.ikora.model.Position;

public class InternalError extends LocalError {
    public InternalError(String message, Position position) {
        super(message, position);
    }
}
