package lu.uni.serval.robotframework.execution.selenium;

import lu.uni.serval.robotframework.report.ExecutionResult;
import org.openqa.selenium.WebElement;

public class FormElement {
    private Context context;
    private ElementFinder elementFinder;

    public FormElement(Context context) {
        this.context = context;
        this.elementFinder = new ElementFinder(this.context);
    }

    public ExecutionResult ClickButton(String locator) {
        ExecutionResult result = new ExecutionResult(ExecutionResult.Type.Execute);
        findElement(locator).click();

        if(!result.isError()) {
            result.setMessage("Click on button " + locator);
        }

        return result;
    }

    public ExecutionResult InputPassword(String locator, String input) {
        ExecutionResult result = new ExecutionResult(ExecutionResult.Type.Execute);
        inputTextIntoTextField(result, locator, input);

        if (!result.isError()) {
            result.setMessage("Input text in element '" + locator + "' using password '" + input + "'");
        }

        return result;
    }

    public ExecutionResult InputText(String locator, String input) {
        ExecutionResult result = new ExecutionResult(ExecutionResult.Type.Execute);
        inputTextIntoTextField(result, locator, input);

        if (!result.isError()) {
            result.setMessage("Input text in element '" + locator + "' using text '" + input + "'");
        }

        return result;
    }

    private WebElement findElement(String locator) {
        return elementFinder.find(locator);
    }

    private void inputTextIntoTextField(ExecutionResult result, String locator, String input) {
        WebElement element = findElement(locator);

        if(element != null) {
            element.sendKeys(input);
        }
        else {
            result.setElementNotFoundError(locator);
        }
    }
}
