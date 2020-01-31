package org.ikora.libraries.selenium.form_element;

import org.ikora.model.Argument;
import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class ClickButton extends LibraryKeyword {
    public ClickButton(){
        this.type = Type.ACTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Argument.Type[] getArgumentTypes() {
        return new Argument.Type[]{
                Argument.Type.LOCATOR
        };
    }
}
