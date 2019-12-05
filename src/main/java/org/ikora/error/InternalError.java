package org.ikora.error;

import org.ikora.model.LineRange;

import java.io.File;

public class InternalError extends LocalError {
    public InternalError(String message, File file, LineRange lines) {
        super(message, file, lines);
    }
}
