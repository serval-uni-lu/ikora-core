package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.StringType;

public class KeywordShouldExist extends LibraryKeyword {
    public KeywordShouldExist(){
        super(Type.ASSERTION,
           new StringType("name"),
           new StringType("message", "None")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
