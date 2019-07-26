package org.ukwikora.libraries.builtin;

import org.ukwikora.model.Value;
import org.ukwikora.model.LibraryKeyword;
import org.ukwikora.runner.Runtime;

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
    public int[] getKeywordsLaunchedPosition() {
        return new int[]{1};
    }
}
