package org.ukwikora.libraries.selenium.alert;

import org.ukwikora.model.LibraryKeyword;
import org.ukwikora.runner.Runtime;

public class AlertShouldBePresent extends LibraryKeyword {
    public AlertShouldBePresent(){
        this.type = Type.Assertion;
    }

    @Override
    public void execute(Runtime runtime) {

    }
}
