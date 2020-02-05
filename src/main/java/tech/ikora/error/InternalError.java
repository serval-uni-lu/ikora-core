package tech.ikora.error;

import tech.ikora.model.Position;

public class InternalError extends LocalError {
    public InternalError(String message, Position position) {
        super(message, position);
    }
}
