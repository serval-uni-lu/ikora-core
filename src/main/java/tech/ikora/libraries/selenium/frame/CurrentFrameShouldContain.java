package tech.ikora.libraries.selenium.frame;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class CurrentFrameShouldContain extends LibraryKeyword {
    public CurrentFrameShouldContain(){
        this.type = Type.ASSERTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
