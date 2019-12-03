package org.ukwikora.error;

import org.ukwikora.model.LineRange;

import java.io.File;

public class SyntaxError extends LocalError {
    public SyntaxError(String message, File file, LineRange lines) {
        super(message, file, lines);
    }
}
