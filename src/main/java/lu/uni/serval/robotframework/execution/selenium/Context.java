package lu.uni.serval.robotframework.execution.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Context {
    public enum Drivers{
        Firefox, Chrome
    }

    private WebDriver driver;

    public WebDriver getDriver() {
        return driver;
    }

    public void initializeDriver(Drivers driver) {
        switch (driver){
            case Chrome:
                this.driver = new ChromeDriver();
            case Firefox:
                this.driver = new FirefoxDriver();
        }
    }
}
