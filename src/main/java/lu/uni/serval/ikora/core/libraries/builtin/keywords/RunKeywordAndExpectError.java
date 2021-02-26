package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.KeywordType;
import lu.uni.serval.ikora.core.types.StringType;

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
