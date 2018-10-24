package org.ukwikora.libraries.selenium.browserManagement;

import org.ukwikora.model.LibraryKeyword;
import org.ukwikora.model.Runtime;


public class SetSeleniumSpeed extends LibraryKeyword {
    public SetSeleniumSpeed(){
        this.type = Type.Synchronisation;
    }

    @Override
    public void execute(Runtime runtime) {
        int sec = 0;

        //TODO: properly convert time
        //getDriver().manage().timeouts().implicitlyWait(sec, TimeUnit.SECONDS);
    }
}
