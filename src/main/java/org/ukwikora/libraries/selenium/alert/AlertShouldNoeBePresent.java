package org.ukwikora.libraries.selenium.alert;

import org.ukwikora.model.LibraryKeyword;
import org.ukwikora.runner.Runtime;

public class AlertShouldNoeBePresent extends LibraryKeyword {
    public AlertShouldNoeBePresent(){
        this.type = Type.Assertion;
    }

    @Override
    public void run(Runtime runtime) {

    }
}
