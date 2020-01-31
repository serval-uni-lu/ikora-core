package org.ikora.libraries.builtin;

import org.ikora.model.Argument;
import org.ikora.model.Value;
import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class RunKeywordIf extends LibraryKeyword {
    public RunKeywordIf(){
        this.type = Type.CONTROL_FLOW;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Argument.Type[] getArgumentTypes() {
        return new Argument.Type[]{
                Argument.Type.CONDITION,
                Argument.Type.KEYWORD,
                Argument.Type.KWARGS
        };
    }
}
