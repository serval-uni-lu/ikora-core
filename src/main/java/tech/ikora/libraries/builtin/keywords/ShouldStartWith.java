package tech.ikora.libraries.builtin.keywords;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class ShouldStartWith extends LibraryKeyword {
    public ShouldStartWith(){
        this.type = Type.ASSERTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}