package lu.uni.serval.robotframework.libraries.selenium.browserManagement;

import lu.uni.serval.robotframework.model.LibraryKeyword;
import lu.uni.serval.robotframework.runner.Runtime;

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
