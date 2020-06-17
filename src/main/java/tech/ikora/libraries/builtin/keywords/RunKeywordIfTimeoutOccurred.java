package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.KeywordType;

public class RunKeywordIfTimeoutOccurred extends LibraryKeyword {
    public RunKeywordIfTimeoutOccurred(){
        super(Type.CONTROL_FLOW, new KeywordType("keyword"));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
