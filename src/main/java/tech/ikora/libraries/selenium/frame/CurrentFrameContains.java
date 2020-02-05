package tech.ikora.libraries.selenium.frame;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class CurrentFrameContains extends LibraryKeyword {
    public CurrentFrameContains(){
        this.type = Type.ASSERTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
