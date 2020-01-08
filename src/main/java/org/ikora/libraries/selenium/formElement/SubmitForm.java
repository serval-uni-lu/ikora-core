package org.ikora.libraries.selenium.formElement;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class SubmitForm extends LibraryKeyword {
    public SubmitForm(){
        this.type = Type.ACTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
