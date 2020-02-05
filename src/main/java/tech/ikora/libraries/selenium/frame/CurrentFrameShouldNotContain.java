package tech.ikora.libraries.selenium.frame;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class CurrentFrameShouldNotContain extends LibraryKeyword {
    public CurrentFrameShouldNotContain(){
        this.type = Type.ASSERTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
