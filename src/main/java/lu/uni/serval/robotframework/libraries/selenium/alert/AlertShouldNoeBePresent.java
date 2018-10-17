package lu.uni.serval.robotframework.libraries.selenium.alert;

import lu.uni.serval.robotframework.model.LibraryKeyword;
import lu.uni.serval.robotframework.runner.Runtime;

public class AlertShouldNoeBePresent extends LibraryKeyword {
    public AlertShouldNoeBePresent(){
        this.type = Type.Assertion;
    }

    @Override
    public void execute(Runtime runtime) {

    }
}
