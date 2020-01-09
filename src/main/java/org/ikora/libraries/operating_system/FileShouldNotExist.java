package org.ikora.libraries.operating_system;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class FileShouldNotExist extends LibraryKeyword {
    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
