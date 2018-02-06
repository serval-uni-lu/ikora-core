package lu.uni.serval.robotframework.execution.selenium;

import lu.uni.serval.robotframework.report.Result;
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
    public Result InputPassword(List<String> arguments) {
        Result result = new Result();
        inputTextIntoTextField(result, arguments);

        return result;
    }

    @Keyword
    public Result InputText(List<String> arguments) {
        Result result = new Result();
        inputTextIntoTextField(result, arguments);

        return result;
    }

    private WebElement findElement(String locator) {
        return elementFinder.find(locator);
    }

    private void inputTextIntoTextField(Result result, List<String> arguments) {
        String locator = arguments.get(0);
        String input = arguments.get(1);

        findElement(locator).sendKeys(input);
    }
}
