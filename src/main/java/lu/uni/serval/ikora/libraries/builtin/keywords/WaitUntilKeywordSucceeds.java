package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.KeywordType;
import lu.uni.serval.ikora.types.TimeoutType;

public class WaitUntilKeywordSucceeds extends LibraryKeyword {
    public WaitUntilKeywordSucceeds(){
        super(Type.SYNCHRONIZATION,
                new TimeoutType("retry"),
                new TimeoutType("retry_interval"),
                new KeywordType("keyword")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
