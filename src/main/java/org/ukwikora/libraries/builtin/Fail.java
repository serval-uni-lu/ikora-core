package org.ukwikora.libraries.builtin;

import org.ukwikora.model.LibraryKeyword;
import org.ukwikora.model.Runtime;

public class Fail extends LibraryKeyword {
    public Fail(){
        this.type = Type.Error;
    }

    @Override
    public void execute(Runtime runtime) {

    }
}
