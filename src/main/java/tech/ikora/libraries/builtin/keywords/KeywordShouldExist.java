package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;
import tech.ikora.types.StringType;

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
