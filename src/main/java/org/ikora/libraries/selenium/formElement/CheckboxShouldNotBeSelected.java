package org.ikora.libraries.selenium.formElement;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class CheckboxShouldNotBeSelected extends LibraryKeyword {
    public CheckboxShouldNotBeSelected(){
        this.type = Type.ASSERTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
