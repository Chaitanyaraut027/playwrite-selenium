package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.time.Duration;

/**
 * BaseTest manages the WebDriver lifecycle and test setup.
 * Uses Selenium Manager (built into Selenium 4) for automated driver management.
 */
public class BaseTest {

    protected WebDriver driver;
    protected String appUrl;

    @BeforeMethod
    public void setUp() {
        // Configure ChromeOptions
        ChromeOptions options = new ChromeOptions();

        // Check if headless mode is enabled (default: true for reliable CI and automation)
        String headlessProp = System.getProperty("headless", "true");
        if (Boolean.parseBoolean(headlessProp)) {
            options.addArguments("--headless=new");
        }

        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        // Selenium Manager automatically locates and prepares ChromeDriver
        driver = new ChromeDriver(options);

        // Standard timeouts
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(15));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        driver.manage().window().maximize();

        // Resolve application URL
        appUrl = resolveAppUrl();
        driver.get(appUrl);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public WebDriver getDriver() {
        return driver;
    }

    /**
     * Resolves the application path dynamically regardless of execution root.
     * Can also be overridden with -Dapp.url system property.
     */
    private String resolveAppUrl() {
        String overrideUrl = System.getProperty("app.url");
        if (overrideUrl != null && !overrideUrl.trim().isEmpty()) {
            return overrideUrl.trim();
        }

        File appFile = new File("../application/index.html");
        if (!appFile.exists()) {
            appFile = new File("application/index.html");
        }

        if (appFile.exists()) {
            return appFile.toURI().toString();
        }

        throw new IllegalStateException("Application index.html could not be located at expected paths.");
    }
}

