package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.Argument;
import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.KeywordType;

public class RunKeywordIfAllTestsPassed extends LibraryKeyword {
    public RunKeywordIfAllTestsPassed(){
        super(Type.CONTROL_FLOW, new KeywordType("keyword"));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
