package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class ReturnFromKeyword extends LibraryKeyword {
    public ReturnFromKeyword(){
        this.type = Type.CONTROL_FLOW;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
