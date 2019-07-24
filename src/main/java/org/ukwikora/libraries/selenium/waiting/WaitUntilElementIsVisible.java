package org.ukwikora.libraries.selenium.waiting;

import org.ukwikora.model.Value;
import org.ukwikora.model.LibraryKeyword;
import org.ukwikora.runner.Runtime;

public class WaitUntilElementIsVisible extends LibraryKeyword {
    public WaitUntilElementIsVisible(){
        this.type = Type.Synchronisation;
    }

    @Override
    public void execute(Runtime runtime) {

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
