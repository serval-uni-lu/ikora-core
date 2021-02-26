package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.KeywordType;
import lu.uni.serval.ikora.core.types.TimeoutType;

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
