package tech.ikora.libraries.selenium.form_element;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class PageShouldNotContainTextfield extends LibraryKeyword {
    public PageShouldNotContainTextfield(){
        this.type = Type.ASSERTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
