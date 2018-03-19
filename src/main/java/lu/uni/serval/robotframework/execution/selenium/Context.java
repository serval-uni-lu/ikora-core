package lu.uni.serval.robotframework.execution.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class Context {
    public enum Drivers{
        Firefox, Chrome
    }

    private WebDriver driver;

    public WebDriver getDriver() {
        return driver;
    }

    public void initializeDriver(Drivers driver, String url) {

        switch (driver){
            case Chrome:
                ChromeOptions chromeOptions = new ChromeOptions();
                this.driver = new ChromeDriver(chromeOptions);
                break;

            case Firefox:
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                this.driver = new FirefoxDriver(firefoxOptions);
                break;
        }

        this.driver.get(url);
    }

    public void checkRequests() {
    }
}
