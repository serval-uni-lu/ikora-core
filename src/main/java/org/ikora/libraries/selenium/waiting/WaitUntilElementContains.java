package org.ikora.libraries.selenium.waiting;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class WaitUntilElementContains extends LibraryKeyword {
    public WaitUntilElementContains(){
        this.type = Type.SYNCHRONISATION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
