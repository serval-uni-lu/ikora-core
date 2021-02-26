package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.KeywordType;
import lu.uni.serval.ikora.core.types.StringType;

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
