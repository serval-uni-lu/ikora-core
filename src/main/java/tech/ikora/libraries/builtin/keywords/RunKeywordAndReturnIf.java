package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.ConditionType;
import tech.ikora.types.KeywordType;

public class RunKeywordAndReturnIf extends LibraryKeyword {
    public RunKeywordAndReturnIf(){
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
