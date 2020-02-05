package tech.ikora.error;

import tech.ikora.model.Position;

public class SyntaxError extends LocalError {
    public SyntaxError(String message, Position position) {
        super(message, position);
    }
}
