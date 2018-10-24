package org.ukwikora.libraries.selenium.runOnFailure;

import org.ukwikora.model.LibraryKeyword;
import org.ukwikora.model.Runtime;

public class RegisterKeywordToRunOnFailure extends LibraryKeyword {
    public RegisterKeywordToRunOnFailure(){
        this.type = Type.ControlFlow;
    }

    @Override
    public void execute(Runtime runtime) {

    }
}
