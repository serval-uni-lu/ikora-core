package org.ikora.libraries.builtin;

import org.ikora.model.Value;
import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class RunKeywords extends LibraryKeyword {
    public RunKeywords(){
        this.type = Type.ControlFlow;
    }

    @Override
    public void run(Runtime runtime) {

    }

    @Override
    public Value.Type[] getArgumentTypes() {
        return new Value.Type[]{
                Value.Type.Keywords,
        };
    }

    @Override
    public int[] getKeywordsLaunchedPosition() {
        return new int[]{-1};
    }
}
