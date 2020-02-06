package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.Argument;
import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class RunKeywordAndExpectError extends LibraryKeyword {
    public RunKeywordAndExpectError(){
        this.type = Type.CONTROL_FLOW;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Argument.Type[] getArgumentTypes() {
        return new Argument.Type[]{
                Argument.Type.STRING,
                Argument.Type.KEYWORD,
                Argument.Type.KWARGS
        };
    }
}
