package org.ikora.libraries.selenium.form_element;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class PageShouldNotContainRadioButton extends LibraryKeyword {
    public PageShouldNotContainRadioButton(){
        this.type = Type.ASSERTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
