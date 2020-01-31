package org.ikora.libraries.selenium.waiting;

import org.ikora.model.Argument;
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
    public Argument.Type[] getArgumentTypes() {
        return new Argument.Type[]{
                Argument.Type.LOCATOR,
                Argument.Type.STRING,
                Argument.Type.STRING
        };
    }
}
