package tech.ikora.libraries.selenium.waiting;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class WaitUntilElementDoesNotContain extends LibraryKeyword {
    public WaitUntilElementDoesNotContain(){
        this.type = Type.SYNCHRONISATION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
