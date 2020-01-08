package org.ikora.libraries.selenium.formElement;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class RadioButtonShouldNotBeSelected extends LibraryKeyword {
    public RadioButtonShouldNotBeSelected(){
        this.type = Type.ASSERTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
