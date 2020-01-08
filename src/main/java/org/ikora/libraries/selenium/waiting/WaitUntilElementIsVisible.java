package org.ikora.libraries.selenium.waiting;

import org.ikora.model.Value;
import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class WaitUntilElementIsVisible extends LibraryKeyword {
    public WaitUntilElementIsVisible(){
        this.type = Type.SYNCHRONISATION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Value.Type[] getArgumentTypes() {
        return new Value.Type[]{
                Value.Type.LOCATOR,
                Value.Type.STRING,
                Value.Type.STRING
        };
    }
}
