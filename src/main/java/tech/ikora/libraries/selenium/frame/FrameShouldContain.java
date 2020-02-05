package tech.ikora.libraries.selenium.frame;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class FrameShouldContain extends LibraryKeyword {
    public FrameShouldContain(){
        this.type = Type.GET;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
