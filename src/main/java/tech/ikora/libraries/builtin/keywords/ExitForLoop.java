package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class ExitForLoop extends LibraryKeyword {
    public ExitForLoop(){
        this.type = Type.CONTROL_FLOW;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}