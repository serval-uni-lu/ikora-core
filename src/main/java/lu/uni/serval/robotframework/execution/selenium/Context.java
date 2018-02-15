package lu.uni.serval.robotframework.execution.selenium;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public class Context {
    public enum Drivers{
        Firefox, Chrome
    }

    private WebDriver driver;
    private BrowserMobProxy proxy;

    public WebDriver getDriver() {
        return driver;
    }

    public void initializeDriver(Drivers driver, String url) throws UnknownHostException {
        proxy = new BrowserMobProxyServer();

        proxy.setTrustAllServers(true);
        proxy.start();

        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
        String hostIp = Inet4Address.getLocalHost().getHostAddress();
        seleniumProxy.setHttpProxy(hostIp + ":" + proxy.getPort());
        seleniumProxy.setSslProxy(hostIp + ":" + proxy.getPort());

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, proxy);

        switch (driver){
            case Chrome:
                ChromeOptions chromeOptions = new ChromeOptions();
                this.driver = new ChromeDriver(chromeOptions);
                break;

            case Firefox:
                FirefoxOptions firefoxOptions = new FirefoxOptions(capabilities);
                this.driver = new FirefoxDriver(firefoxOptions);
                break;
        }

        this.driver.get(url);
    }

    public void checkRequests() {
    }
}
