package lu.uni.serval.robotframework.execution.selenium;

import lu.uni.serval.robotframework.report.Result;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class BrowserManagement {
    private Context context;

    public BrowserManagement(Context context) {
        this.context = context;
    }

    @Keyword
    public Result LocationShouldBe(List<String> arguments) {
        String url = arguments.get(0);

        if(url.equalsIgnoreCase(getUrl())){
            System.out.println("Page has correct url: " + url);
        } else {
            System.err.println("Page url should be '" + getUrl() + "' got '" + url + "' instead.");
        }

        return new Result();
    }

    @Keyword
    public Result MaximizeBrowserWindow(List<String> arguments) {
        getDriver().manage().window().maximize();

        return new Result();
    }

    @Keyword
    public Result OpenBrowser(List<String> arguments){
        String url = arguments.get(0);
        String browser = arguments.get(1);

        initializeDriver(browser);

        getDriver().get(url);

        return new Result();
    }

    @Keyword
    public Result SetSeleniumSpeed(List<String> arguments) {
        //TODO: properly convert time
        int time = Integer.parseInt(arguments.get(0));
        getDriver().manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);

        return new Result();
    }

    @Keyword
    public Result TitleShouldBe(List<String> arguments) {
        String title = arguments.get(0);

        if(title.equals(getTitle())) {
            System.out.println("Page has correct title: " + title);
        } else {
            System.err.println("Page title should be '" + getTitle() + "' got '" + title + "' instead.");
        }

        return new Result();
    }

    private WebDriver getDriver() {
        return context.getDriver();
    }

    private String getTitle() {
        return getDriver().getTitle();
    }

    private String getUrl() {
        return getDriver().getCurrentUrl();
    }

    private void initializeDriver(String browser) {
        if(browser.equalsIgnoreCase("FireFox")) {
            context.setDriver(new FirefoxDriver());
        }
    }
}
