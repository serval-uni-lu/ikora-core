package tech.ikora.libraries.builtin;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class KeywordShouldExist extends LibraryKeyword {
    public KeywordShouldExist(){
        this.type = Type.ASSERTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}