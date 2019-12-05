package org.ikora.libraries.builtin;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class ShouldNotBeEmpty extends LibraryKeyword {
    public ShouldNotBeEmpty(){
        this.type = Type.Assertion;
    }

    @Override
    public void run(Runtime runtime) {

    }
}
