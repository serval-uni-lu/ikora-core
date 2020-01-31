package org.ikora.libraries.builtin;

import org.ikora.model.Argument;
import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class RunKeywordIfAnyTestFailed extends LibraryKeyword {
    public RunKeywordIfAnyTestFailed(){
        this.type = Type.CONTROL_FLOW;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Argument.Type[] getArgumentTypes() {
        return new Argument.Type[]{
                Argument.Type.KEYWORD,
                Argument.Type.KWARGS
        };
    }
}
