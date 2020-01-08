package org.ikora.libraries.selenium.waiting;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class WaitUntilPageDoesNotContainElement extends LibraryKeyword {
    public WaitUntilPageDoesNotContainElement(){
        this.type = Type.SYNCHRONISATION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
