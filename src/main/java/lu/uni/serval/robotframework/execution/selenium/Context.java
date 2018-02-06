package lu.uni.serval.robotframework.execution.selenium;

import org.openqa.selenium.WebDriver;

public class Context {
    private WebDriver driver;

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }
}
