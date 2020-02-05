package tech.ikora.libraries.selenium.waiting;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class WaitUntilElementIsNotVisible extends LibraryKeyword {
    public WaitUntilElementIsNotVisible(){
        this.type = Type.SYNCHRONISATION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
