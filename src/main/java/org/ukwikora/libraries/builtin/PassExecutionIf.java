package org.ukwikora.libraries.builtin;

import org.ukwikora.model.LibraryKeyword;
import org.ukwikora.model.Runtime;

public class PassExecutionIf extends LibraryKeyword {
    public PassExecutionIf(){
        this.type = Type.ControlFlow;
    }

    @Override
    public void execute(Runtime runtime) {

    }
}
