package org.ukwikora.libraries.selenium.browserManagement;

import org.ukwikora.model.LibraryKeyword;
import org.ukwikora.runner.Runtime;

public class OpenBrowser extends LibraryKeyword {
    public OpenBrowser(){
        this.type = Type.Action;
    }

    @Override
    public void execute(Runtime runtime) {
        String browser = "browser";
        String url = "url";

        //initializeDriver(browser, url);
    }
}
