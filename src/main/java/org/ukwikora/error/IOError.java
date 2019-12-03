package org.ukwikora.error;

import java.io.File;

public class IOError extends Error {
    private File file;

    public IOError(String message, File file) {
        super(message);
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}
