package org.ikora.libraries.selenium.frame;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class UnselectFrame extends LibraryKeyword {
    public UnselectFrame(){
        this.type = Type.ACTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
