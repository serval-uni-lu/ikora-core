package tech.ikora.libraries.selenium.alert;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class AlertShouldNoeBePresent extends LibraryKeyword {
    public AlertShouldNoeBePresent(){
        this.type = Type.ASSERTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
