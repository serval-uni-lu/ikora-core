package lu.uni.serval.robotframework.execution.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ElementFinder {
    private Context context;

    public ElementFinder(Context context) {
        this.context = context;
    }

    public WebElement find(String locator) {
        //TODO: manage other types of locators
        return context.getDriver().findElement(By.id(locator));
    }
}
