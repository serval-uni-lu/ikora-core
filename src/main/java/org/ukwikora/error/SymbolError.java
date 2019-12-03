package org.ukwikora.error;

import org.ukwikora.model.LineRange;

import java.io.File;

public class SymbolError extends LocalError {
    public SymbolError(String message, File file, LineRange lines) {
        super(message, file, lines);
    }
}
