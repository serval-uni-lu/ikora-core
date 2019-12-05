package org.ikora.libraries.selenium.formElement;

import org.ikora.model.Value;
import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class ClickButton extends LibraryKeyword {
    public ClickButton(){
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
