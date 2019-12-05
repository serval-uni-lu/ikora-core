package org.ikora.libraries.builtin;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class Fail extends LibraryKeyword {
    public Fail(){
        this.type = Type.Error;
    }

    @Override
    public void run(Runtime runtime) {

    }
}
