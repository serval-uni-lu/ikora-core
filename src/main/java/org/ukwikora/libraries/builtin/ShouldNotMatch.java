package org.ukwikora.libraries.builtin;

import org.ukwikora.model.LibraryKeyword;
import org.ukwikora.model.Runtime;

public class ShouldNotMatch extends LibraryKeyword {
    public ShouldNotMatch(){
        this.type = Type.Assertion;
    }

    @Override
    public void execute(Runtime runtime) {

    }
}
