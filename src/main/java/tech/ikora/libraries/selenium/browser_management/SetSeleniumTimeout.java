package tech.ikora.libraries.selenium.browser_management;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class SetSeleniumTimeout extends LibraryKeyword {
    public SetSeleniumTimeout(){
        this.type = Type.SYNCHRONISATION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
