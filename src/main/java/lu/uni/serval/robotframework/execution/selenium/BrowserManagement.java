package lu.uni.serval.robotframework.execution.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class BrowserManagement {
    private WebDriver driver;

    @Keyword
    public void MaximizeBrowserWindow(List<String> arguments) {
        driver.manage().window().maximize();
    }

    @Keyword
    public void OpenBrowser(List<String> arguments){
        if(arguments.get(1).equalsIgnoreCase("FireFox")) {
            driver = new FirefoxDriver();
        }

        driver.get(arguments.get(0));
    }

    @Keyword
    public void SetSeleniumSpeed(List<String> arguments) {
        //TODO: properly convert time
        int time = Integer.parseInt(arguments.get(0));
        driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
    }

    @Keyword
    public void TitleShouldBe(List<String> arguments) {
        String title = arguments.get(0);

        if(title.equals(GetTitle())) {
            System.out.println("Page name has right title: " + title);
        } else {
            System.err.println("Page title should be '" + GetTitle() + "' got '" + title + "' instead.");
        }
    }

    private String GetTitle() {
        return driver.getTitle();
    }


}
