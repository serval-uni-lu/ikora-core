package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.KeywordType;
import lu.uni.serval.ikora.types.StringType;

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
