package tech.ikora.libraries.selenium.waiting;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class WaitForCondition extends LibraryKeyword {
    public WaitForCondition(){
        this.type = Type.SYNCHRONISATION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
