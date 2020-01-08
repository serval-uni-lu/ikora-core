package org.ikora.libraries.selenium.alert;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class ConfirmAction extends LibraryKeyword {
    public ConfirmAction(){
        this.type = Type.ACTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
