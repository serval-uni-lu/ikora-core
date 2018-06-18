package lu.uni.serval.robotframework.libraries.selenium.browserManagement;

import lu.uni.serval.robotframework.model.LibraryKeyword;

import java.util.concurrent.TimeUnit;

public class SetSeleniumSpeed extends LibraryKeyword {
    @Override
    public void execute() {
        int sec = 0;

        //TODO: properly convert time
        //getDriver().manage().timeouts().implicitlyWait(sec, TimeUnit.SECONDS);
    }
}
