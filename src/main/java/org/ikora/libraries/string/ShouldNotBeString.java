package org.ikora.libraries.string;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class ShouldNotBeString extends LibraryKeyword {
    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
