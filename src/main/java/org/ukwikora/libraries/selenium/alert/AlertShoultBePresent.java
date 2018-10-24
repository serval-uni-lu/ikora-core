package org.ukwikora.libraries.selenium.alert;

import org.ukwikora.model.LibraryKeyword;
import org.ukwikora.model.Runtime;

public class AlertShoultBePresent extends LibraryKeyword {
    public AlertShoultBePresent(){
        this.type = Type.Assertion;
    }

    @Override
    public void execute(Runtime runtime) {

    }
}
