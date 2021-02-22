package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.KeywordType;

public class RunKeywordIfTimeoutOccurred extends LibraryKeyword {
    public RunKeywordIfTimeoutOccurred(){
        super(Type.CONTROL_FLOW, new KeywordType("keyword"));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
