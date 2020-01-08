package org.ikora.libraries.builtin;

import org.ikora.model.Value;
import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class RunKeywordUnless extends LibraryKeyword {
    public RunKeywordUnless(){
        this.type = Type.CONTROL_FLOW;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Value.Type[] getArgumentTypes() {
        return new Value.Type[]{
                Value.Type.CONDITION,
                Value.Type.KEYWORD,
                Value.Type.KWARGS
        };
    }


    @Override
    public int getMaxNumberArguments() {
        return -1;
    }
}
