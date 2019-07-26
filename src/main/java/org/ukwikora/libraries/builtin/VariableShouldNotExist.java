package org.ukwikora.libraries.builtin;

import org.ukwikora.model.LibraryKeyword;
import org.ukwikora.runner.Runtime;

public class VariableShouldNotExist extends LibraryKeyword {
    public VariableShouldNotExist(){
        this.type = Type.Assertion;
    }

    @Override
    public void run(Runtime runtime) {

    }
}
