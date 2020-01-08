package org.ikora.libraries.selenium.formElement;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class PageShouldNotContainTextfield extends LibraryKeyword {
    public PageShouldNotContainTextfield(){
        this.type = Type.ASSERTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
