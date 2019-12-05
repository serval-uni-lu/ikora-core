package org.ikora.libraries.selenium.browserManagement;

import org.ikora.model.LibraryKeyword;
import org.ikora.runner.Runtime;


public class SetSeleniumSpeed extends LibraryKeyword {
    public SetSeleniumSpeed(){
        this.type = Type.Synchronisation;
    }

    @Override
    public void run(Runtime runtime) {
        int sec = 0;

        //TODO: properly convert time
        //getDriver().manage().timeouts().implicitlyWait(sec, TimeUnit.SECONDS);
    }
}
