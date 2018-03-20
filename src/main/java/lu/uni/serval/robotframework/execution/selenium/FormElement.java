package lu.uni.serval.robotframework.execution.selenium;

import lu.uni.serval.robotframework.report.Result;
import org.openqa.selenium.WebElement;

public class FormElement {
    private Context context;
    private ElementFinder elementFinder;

    public FormElement(Context context) {
        this.context = context;
        this.elementFinder = new ElementFinder(this.context);
    }

    @Keyword
    public Result ClickButton(String locator) {
        Result result = new Result(Result.Type.Execute);
        findElement(locator).click();

        if(!result.isError()) {
            result.setMessage("Click on button " + locator);
        }

        return result;
    }

    @Keyword
    public Result InputPassword(String locator, String input) {
        Result result = new Result(Result.Type.Execute);
        inputTextIntoTextField(result, locator, input);

        if (!result.isError()) {
            result.setMessage("Input text in element '" + locator + "' using password '" + input + "'");
        }

        return result;
    }

    @Keyword
    public Result InputText(String locator, String input) {
        Result result = new Result(Result.Type.Execute);
        inputTextIntoTextField(result, locator, input);

        if (!result.isError()) {
            result.setMessage("Input text in element '" + locator + "' using text '" + input + "'");
        }

        return result;
    }

    private WebElement findElement(String locator) {
        return elementFinder.find(locator);
    }

    private void inputTextIntoTextField(Result result, String locator, String input) {
        WebElement element = findElement(locator);

        if(element != null) {
            element.sendKeys(input);
        }
        else {
            result.setElementNotFoundError(locator);
        }
    }
}
