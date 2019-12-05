package org.ikora.libraries.selenium.runOnFailure;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class RegisterKeywordToRunOnFailure extends LibraryKeyword {
    public RegisterKeywordToRunOnFailure(){
        this.type = Type.ControlFlow;
    }

    @Override
    public void run(Runtime runtime) {

    }
}
