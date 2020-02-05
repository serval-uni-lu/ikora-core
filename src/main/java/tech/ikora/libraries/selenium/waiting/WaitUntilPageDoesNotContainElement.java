package tech.ikora.libraries.selenium.waiting;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class WaitUntilPageDoesNotContainElement extends LibraryKeyword {
    public WaitUntilPageDoesNotContainElement(){
        this.type = Type.SYNCHRONISATION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
