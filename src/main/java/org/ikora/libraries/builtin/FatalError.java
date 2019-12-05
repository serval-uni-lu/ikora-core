package org.ikora.libraries.builtin;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class FatalError extends LibraryKeyword {
    public FatalError(){
        this.type = Type.Error;
    }

    @Override
    public void run(Runtime runtime) {

    }
}
