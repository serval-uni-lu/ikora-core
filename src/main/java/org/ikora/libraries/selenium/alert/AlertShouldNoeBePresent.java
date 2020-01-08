package org.ikora.libraries.selenium.alert;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class AlertShouldNoeBePresent extends LibraryKeyword {
    public AlertShouldNoeBePresent(){
        this.type = Type.ASSERTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
