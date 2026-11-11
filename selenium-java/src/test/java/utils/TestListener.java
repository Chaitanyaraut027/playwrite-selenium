package utils;

import base.BaseTest;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TestListener handles test lifecycle events in TestNG,
 * specifically capturing screenshots automatically when tests fail.
 */
public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("[STARTED] Executing Test: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("[PASSED]  Test Passed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.err.println("[FAILED]  Test Failed: " + result.getMethod().getMethodName());
        System.err.println("Reason: " + result.getThrowable().getMessage());

        Object currentClass = result.getInstance();
        if (currentClass instanceof BaseTest) {
            WebDriver driver = ((BaseTest) currentClass).getDriver();
            if (driver != null) {
                captureScreenshot(driver, result.getMethod().getMethodName());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("[SKIPPED] Test Skipped: " + result.getMethod().getMethodName());
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("========== Starting Suite: " + context.getName() + " ==========");
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("========== Finished Suite: " + context.getName() + " ==========");
    }

    /**
     * Captures a screenshot from the active WebDriver instance and saves it to the screenshots directory.
     *
     * @param driver   The WebDriver instance
     * @param testName The name of the failed test
     */
    public static String captureScreenshot(WebDriver driver, String testName) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = "FAIL_" + testName + "_" + timestamp + ".png";

            // Resolve target directory (root screenshots folder)
            Path targetDir = Paths.get("..", "screenshots");
            if (!Files.exists(targetDir)) {
                targetDir = Paths.get("screenshots");
            }
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }

            Path destination = targetDir.resolve(fileName);
            Files.copy(source.toPath(), destination);
            System.out.println("[SCREENSHOT] Failure screenshot saved to: " + destination.toAbsolutePath());
            return destination.toAbsolutePath().toString();
        } catch (IOException e) {
            System.err.println("[SCREENSHOT ERROR] Could not save screenshot: " + e.getMessage());
            return null;
        }
    }
}

