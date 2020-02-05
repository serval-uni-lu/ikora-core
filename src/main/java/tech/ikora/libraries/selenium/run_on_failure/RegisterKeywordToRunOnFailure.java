package tech.ikora.libraries.selenium.run_on_failure;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class RegisterKeywordToRunOnFailure extends LibraryKeyword {
    public RegisterKeywordToRunOnFailure(){
        this.type = Type.CONTROL_FLOW;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
