package org.ikora.libraries.selenium.browserManagement;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;

public class OpenBrowser extends LibraryKeyword {
    public OpenBrowser(){
        this.type = Type.Action;
    }

    @Override
    public void run(Runtime runtime) {
        String browser = "browser";
        String url = "url";

        //initializeDriver(browser, url);
    }
}
