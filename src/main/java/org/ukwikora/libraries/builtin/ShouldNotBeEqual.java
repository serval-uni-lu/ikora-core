package org.ukwikora.libraries.builtin;

import org.ukwikora.model.LibraryKeyword;
import org.ukwikora.model.Runtime;

public class ShouldNotBeEqual extends LibraryKeyword {
    public ShouldNotBeEqual(){
        this.type = Type.Assertion;
    }

    @Override
    public void execute(Runtime runtime) {

    }
}
