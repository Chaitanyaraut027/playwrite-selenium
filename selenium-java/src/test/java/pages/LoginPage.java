package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * LoginPage implements the Page Object Model (POM) pattern for the Login and Dashboard view.
 * Encapsulates element locators and user interactions, keeping test logic decoupled from UI structure.
 * Employs explicit waits (WebDriverWait) to prevent flaky timing issues.
 */
public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators - Login Section
    private final By loginSection = By.id("login-section");
    private final By loginForm = By.id("login-form");
    private final By usernameInput = By.id("username");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-btn");
    private final By errorMessage = By.id("error-message");
    private final By successMessage = By.id("success-message");

    // Locators - Dashboard Section
    private final By dashboardSection = By.id("dashboard");
    private final By dashboardTitle = By.id("dashboard-title");
    private final By welcomeUser = By.id("welcome-user");
    private final By logoutButton = By.id("logout-btn");
    private final By statCards = By.className("stat-card");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    /**
     * Enters username into the username input field.
     */
    public LoginPage enterUsername(String username) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInput));
        input.clear();
        if (username != null && !username.isEmpty()) {
            input.sendKeys(username);
        }
        return this;
    }

    /**
     * Enters password into the password input field.
     */
    public LoginPage enterPassword(String password) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput));
        input.clear();
        if (password != null && !password.isEmpty()) {
            input.sendKeys(password);
        }
        return this;
    }

    /**
     * Clicks the login submit button.
     */
    public void clickLogin() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        button.click();
    }

    /**
     * Convenience method to perform complete login action.
     */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    /**
     * Clicks the logout button on the dashboard.
     */
    public void clickLogout() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(logoutButton));
        button.click();
    }

    /**
     * Retrieves visible error message text with explicit wait.
     */
    public String getErrorMessage() {
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
        return error.getText().trim();
    }

    /**
     * Retrieves visible success message text with explicit wait.
     */
    public String getSuccessMessage() {
        WebElement success = wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
        return success.getText().trim();
    }

    /**
     * Checks if the error message banner is visible.
     */
    public boolean isErrorMessageDisplayed() {
        try {
            WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            return error.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if the success message banner is visible.
     */
    public boolean isSuccessMessageDisplayed() {
        try {
            WebElement success = wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
            return success.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if the dashboard section is visible.
     */
    public boolean isDashboardDisplayed() {
        try {
            WebElement dash = wait.until(ExpectedConditions.visibilityOfElementLocated(dashboardSection));
            return dash.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if the login form/card is visible.
     */
    public boolean isLoginFormDisplayed() {
        try {
            WebElement form = wait.until(ExpectedConditions.visibilityOfElementLocated(loginSection));
            return form.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the welcome greeting text from the dashboard.
     */
    public String getWelcomeText() {
        WebElement welcome = wait.until(ExpectedConditions.visibilityOfElementLocated(welcomeUser));
        return welcome.getText().trim();
    }

    /**
     * Checks if logout button is displayed on dashboard.
     */
    public boolean isLogoutButtonDisplayed() {
        try {
            WebElement button = wait.until(ExpectedConditions.visibilityOfElementLocated(logoutButton));
            return button.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Counts stat summary cards on the dashboard.
     */
    public int getDashboardStatCardsCount() {
        return driver.findElements(statCards).size();
    }

    /**
     * Gets the current value inside the username input.
     */
    public String getUsernameInputValue() {
        return driver.findElement(usernameInput).getAttribute("value");
    }

    /**
     * Gets the current value inside the password input.
     */
    public String getPasswordInputValue() {
        return driver.findElement(passwordInput).getAttribute("value");
    }
}

