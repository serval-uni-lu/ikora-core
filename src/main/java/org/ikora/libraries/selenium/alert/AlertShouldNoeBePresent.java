package org.ikora.libraries.selenium.alert;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class AlertShouldNoeBePresent extends LibraryKeyword {
    public AlertShouldNoeBePresent(){
        this.type = Type.Assertion;
    }

    @Override
    public void run(Runtime runtime) {

    }
}
