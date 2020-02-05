package tech.ikora.libraries.selenium.form_element;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class SubmitForm extends LibraryKeyword {
    public SubmitForm(){
        this.type = Type.ACTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
