package org.ukwikora.libraries.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class SeleniumDriver {
    static public final String name = "Selenium2";

    public enum Drivers{
        Firefox, Chrome, Unknown
    }

    private WebDriver driver;

    public WebDriver getDriver() {
        return driver;
    }

    public void initialize(String browser, String url) {
        Drivers driver = Drivers.Unknown;

        for(Drivers current: Drivers.values()) {
            if(browser.compareToIgnoreCase(current.name()) == 0) {
                driver = current;
                break;
            }
        }

        initialize(driver, url);
    }

    public void initialize(Drivers driver, String url) {

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
}
