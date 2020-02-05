package tech.ikora.libraries.selenium.browser_management;

import tech.ikora.model.LibraryKeyword;
import tech.ikora.runner.Runtime;

public class CloseAllBrowsers extends LibraryKeyword {
    public CloseAllBrowsers(){
        this.type = Type.ACTION;
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
