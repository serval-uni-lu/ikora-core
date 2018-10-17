package lu.uni.serval.robotframework.libraries.selenium.browserManagement;

import lu.uni.serval.robotframework.model.LibraryKeyword;
import lu.uni.serval.robotframework.runner.Runtime;


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
