package tech.ikora.libraries.selenium.frame;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class UnselectFrame extends LibraryKeyword {
    public UnselectFrame(){
        this.type = Type.ACTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
