package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.KeywordType;

import java.util.Collections;

public class RunKeywordAndContinueOnFailure extends LibraryKeyword {
    public RunKeywordAndContinueOnFailure(){
        super(Type.CONTROL_FLOW, Collections.singletonList(new KeywordType("keyword")));
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
