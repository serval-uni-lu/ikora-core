package lu.uni.serval.robotframework.selenium.keyword;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.List;

public class BrowserManagement {
    private WebDriver driver;

    @Keyword
    public void OpenBrowser(List<String> arguments){
        if(arguments.get(1).equalsIgnoreCase("FireFox")) {
            driver = new FirefoxDriver();
        }

        driver.get(arguments.get(0));
    }

    void NonKeyword(){

    }
}
