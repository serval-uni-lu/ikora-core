package org.ukwikora.libraries.selenium.alert;

import org.ukwikora.model.LibraryKeyword;
import org.ukwikora.model.Runtime;

public class AlertShouldNoeBePresent extends LibraryKeyword {
    public AlertShouldNoeBePresent(){
        this.type = Type.Assertion;
    }

    @Override
    public void execute(Runtime runtime) {

    }
}
