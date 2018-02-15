package lu.uni.serval.robotframework.execution.selenium;

import lu.uni.serval.robotframework.report.Result;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

public class BrowserManagement {
    private Context context;

    public BrowserManagement(Context context) {
        this.context = context;
    }

    @Keyword
    public Result LocationShouldBe(String url) {
        Result result = new Result(Result.Type.Assert);

        if(url.equalsIgnoreCase(getUrl())){
            result.setMessage("Page has correct url: " + url);
        } else {
            result.setAssertionFailedError("Page url should be '" + url + "' got '" + getUrl() + "' instead.");
        }

        return result;
    }

    @Keyword
    public Result MaximizeBrowserWindow() {
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)Math.round(screenSize.getWidth());
        int height = (int)Math.round(screenSize.getHeight());

        Result result = new Result(Result.Type.Execute);
        getDriver().manage().window().setSize(new Dimension(width, height));
        result.setMessage("Browser Window Maximized");

        return result;
    }

    @Keyword
    public Result OpenBrowser(String url, String browser) throws UnknownHostException {
        Result result = new Result(Result.Type.Execute);

        initializeDriver(browser, url);
        result.setMessage(browser + " browser open at page " + url);

        return result;
    }

    @Keyword
    public Result SetSeleniumSpeed(String time) {
        Result result = new Result(Result.Type.Execute);

        //TODO: properly convert time
        int sec = Integer.parseInt(time);
        getDriver().manage().timeouts().implicitlyWait(sec, TimeUnit.SECONDS);
        result.setMessage("Selenium speed set to " + time);

        return result;
    }

    @Keyword
    public Result TitleShouldBe(String title) {
        Result result = new Result(Result.Type.Assert);

        if(title.equals(getTitle())) {
            result.setMessage("Page has correct title: " + title);
        } else {
            result.setAssertionFailedError("Page title should be '" + title + "' got '" + getTitle() + "' instead.");
        }

        return result;
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

    private void initializeDriver(String browser, String url) throws UnknownHostException {
        if(browser.equalsIgnoreCase("FireFox")) {
            context.initializeDriver(Context.Drivers.Firefox, url);
        }
        else if(browser.equalsIgnoreCase("Chrome")) {
            context.initializeDriver(Context.Drivers.Chrome, url);
        }
    }
}
