package org.ikora.libraries.builtin;

import org.ikora.model.Value;
import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class RunKeywordIfTestPassed extends LibraryKeyword {
    public RunKeywordIfTestPassed(){
        this.type = Type.ControlFlow;
    }

    @Override
    public void run(Runtime runtime) {

    }

    @Override
    public Value.Type[] getArgumentTypes() {
        return new Value.Type[]{
                Value.Type.Keyword,
                Value.Type.Kwargs
        };
    }

    @Override
    public int[] getKeywordsLaunchedPosition() {
        return new int[]{0};
    }
}
