package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.ConditionType;
import lu.uni.serval.ikora.types.KeywordType;

public class RunKeywordUnless extends LibraryKeyword {
    public RunKeywordUnless(){
        super(Type.CONTROL_FLOW,
                new ConditionType("condition"),
                new KeywordType("keyword")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
