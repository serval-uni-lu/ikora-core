package org.ukwikora.libraries.builtin;

import org.ukwikora.model.LibraryKeyword;
import org.ukwikora.runner.Runtime;

public class FatalError extends LibraryKeyword {
    public FatalError(){
        this.type = Type.Error;
    }

    @Override
    public void run(Runtime runtime) {

    }
}
