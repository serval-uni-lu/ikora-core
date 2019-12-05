package org.ikora.error;

import org.ikora.model.LineRange;

import java.io.File;

public class SyntaxError extends LocalError {
    public SyntaxError(String message, File file, LineRange lines) {
        super(message, file, lines);
    }
}
