package org.ikora.libraries.selenium.element;

import org.ikora.model.Value;
import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class ClickLink extends LibraryKeyword {
    public ClickLink(){
        this.type = Type.Action;
    }

    @Override
    public void run(Runtime runtime) {

    }

    @Override
    public Value.Type[] getArgumentTypes() {
        return new Value.Type[]{
                Value.Type.Locator
        };
    }
}
