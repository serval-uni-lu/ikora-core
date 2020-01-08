package org.ikora.libraries.selenium.alert;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class AlertShouldBePresent extends LibraryKeyword {
    public AlertShouldBePresent(){
        this.type = Type.ASSERTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
