package org.ikora.libraries.builtin;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class Sleep extends LibraryKeyword {
    public Sleep(){
        this.type = Type.Synchronisation;
    }

    @Override
    public void run(Runtime runtime) {

    }
}
