package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.KeywordType;
import lu.uni.serval.ikora.types.StringType;

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
