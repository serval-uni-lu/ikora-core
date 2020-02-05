package tech.ikora.libraries.builtin;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class ContinueForLoopIf extends LibraryKeyword {
    public ContinueForLoopIf(){
        this.type = Type.CONTROL_FLOW;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}