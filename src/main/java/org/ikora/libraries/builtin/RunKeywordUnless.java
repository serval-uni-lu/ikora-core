package org.ikora.libraries.builtin;

import org.ikora.model.Value;
import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class RunKeywordUnless extends LibraryKeyword {
    public RunKeywordUnless(){
        this.type = Type.ControlFlow;
    }

    @Override
    public void run(Runtime runtime) {

    }

    @Override
    public Value.Type[] getArgumentTypes() {
        return new Value.Type[]{
                Value.Type.Condition,
                Value.Type.Keyword,
                Value.Type.Kwargs
        };
    }


    @Override
    public int getMaxNumberArguments() {
        return -1;
    }
}
