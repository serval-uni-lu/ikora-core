package tech.ikora.libraries.selenium.browser_management;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class LocationShouldBe extends LibraryKeyword {
    public LocationShouldBe(){
        this.type = Type.ASSERTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}