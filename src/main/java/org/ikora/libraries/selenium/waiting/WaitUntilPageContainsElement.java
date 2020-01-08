package org.ikora.libraries.selenium.waiting;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class WaitUntilPageContainsElement extends LibraryKeyword {
    public WaitUntilPageContainsElement(){
        this.type = Type.SYNCHRONISATION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
