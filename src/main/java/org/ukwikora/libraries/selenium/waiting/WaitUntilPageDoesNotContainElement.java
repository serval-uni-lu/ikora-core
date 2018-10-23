package org.ukwikora.libraries.selenium.waiting;

import org.ukwikora.model.LibraryKeyword;
import org.ukwikora.runner.Runtime;

public class WaitUntilPageDoesNotContainElement extends LibraryKeyword {
    public WaitUntilPageDoesNotContainElement(){
        this.type = Type.Synchronisation;
    }

    @Override
    public void execute(Runtime runtime) {

    }
}
