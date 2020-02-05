package tech.ikora.libraries.builtin;

import tech.ikora.model.Argument;
import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class RunKeywordAndContinueOnFailure extends LibraryKeyword {
    public RunKeywordAndContinueOnFailure(){
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