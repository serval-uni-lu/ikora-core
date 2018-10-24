package org.ukwikora.libraries.builtin;

import org.ukwikora.model.Value;
import org.ukwikora.model.LibraryKeyword;
import org.ukwikora.model.Runtime;

public class RunKeywordAndExpectError extends LibraryKeyword {
    public RunKeywordAndExpectError(){
        this.type = Type.ControlFlow;
    }

    @Override
    public void execute(Runtime runtime) {

    }

    @Override
    public Value.Type[] getArgumentTypes() {
        return new Value.Type[]{
                Value.Type.String,
                Value.Type.Keyword,
                Value.Type.Kwargs
        };
    }

    @Override
    public int[] getKeywordsLaunchedPosition() {
        return new int[]{1};
    }
}
