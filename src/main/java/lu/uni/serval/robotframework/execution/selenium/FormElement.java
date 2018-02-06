package lu.uni.serval.robotframework.execution.selenium;

import org.openqa.selenium.WebElement;

import java.util.List;

public class FormElement {
    private Context context;
    private ElementFinder elementFinder;

    public FormElement(Context context) {
        this.context = context;
        this.elementFinder = new ElementFinder(this.context);
    }

    @Keyword
    public void ClickButton(List<String> arguments) {
        String locator = arguments.get(0);

        findElement(locator).click();
    }

    @Keyword
    public void InputPassword(List<String> arguments) {
        inputTextIntoTextField(arguments);
    }

    @Keyword
    public void InputText(List<String> arguments) {
        inputTextIntoTextField(arguments);
    }

    private WebElement findElement(String locator) {
        return elementFinder.find(locator);
    }

    private void inputTextIntoTextField(List<String> arguments) {
        String locator = arguments.get(0);
        String input = arguments.get(1);

        findElement(locator).sendKeys(input);
    }
}
