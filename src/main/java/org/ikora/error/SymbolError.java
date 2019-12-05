package org.ikora.error;

import org.ikora.model.LineRange;

import java.io.File;

public class SymbolError extends LocalError {
    public SymbolError(String message, File file, LineRange lines) {
        super(message, file, lines);
    }
}
