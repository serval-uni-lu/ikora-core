package org.ikora.error;

import org.ikora.model.LineRange;

import java.io.File;

public abstract class LocalError extends Error {
    private final File file;
    private final LineRange lines;

    public LocalError(String message, File file, LineRange lines){
        super(message);
        this.file = file;
        this.lines = lines;
    }

    public File getFile() {
        return file;
    }

    public LineRange getLine() {
        return lines;
    }
}
