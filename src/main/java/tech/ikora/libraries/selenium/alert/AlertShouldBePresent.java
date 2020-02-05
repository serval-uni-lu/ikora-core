package tech.ikora.libraries.selenium.alert;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class AlertShouldBePresent extends LibraryKeyword {
    public AlertShouldBePresent(){
        this.type = Type.ASSERTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
