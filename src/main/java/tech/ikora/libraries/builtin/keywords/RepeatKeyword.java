package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.KeywordType;
import tech.ikora.types.StringType;

public class RepeatKeyword extends LibraryKeyword {
    public RepeatKeyword(){
        super(Type.CONTROL_FLOW,
                new StringType("repeat"),
                new KeywordType("keyword")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
