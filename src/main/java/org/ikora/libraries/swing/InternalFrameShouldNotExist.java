package org.ikora.libraries.swing;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class InternalFrameShouldNotExist extends LibraryKeyword {
    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
