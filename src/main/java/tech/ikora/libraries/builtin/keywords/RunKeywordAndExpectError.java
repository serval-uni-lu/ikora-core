package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.KeywordType;
import tech.ikora.types.StringType;

public class RunKeywordAndExpectError extends LibraryKeyword {
    public RunKeywordAndExpectError(){
        super(Type.CONTROL_FLOW,
                new StringType("expected_error"),
                new KeywordType("keyword")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
