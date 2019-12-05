package org.ikora.libraries.selenium.waiting;

import org.ikora.model.Value;
import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class WaitUntilElementIsVisible extends LibraryKeyword {
    public WaitUntilElementIsVisible(){
        this.type = Type.Synchronisation;
    }

    @Override
    public void run(Runtime runtime) {

    }

    @Override
    public Value.Type[] getArgumentTypes() {
        return new Value.Type[]{
                Value.Type.Locator,
                Value.Type.String,
                Value.Type.String
        };
    }
}
