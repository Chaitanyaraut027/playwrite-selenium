package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;

/**
 * LoginTest verifies authentication scenarios against the Techdome QA Application.
 * Covers positive, negative, and boundary test cases using Page Object Model.
 */
public class LoginTest extends BaseTest {

    private LoginPage loginPage;

    // Test Data Constants
    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "admin123";
    private static final String INVALID_USERNAME = "wronguser";
    private static final String INVALID_PASSWORD = "wrongpassword";

    private static final String ERR_INVALID_CREDENTIALS = "Invalid username or password.";
    private static final String ERR_EMPTY_USERNAME = "Username is required.";
    private static final String ERR_EMPTY_PASSWORD = "Password is required.";
    private static final String ERR_BOTH_EMPTY = "Please fill in all fields.";
    private static final String MSG_LOGOUT_SUCCESS = "You have been logged out successfully.";

    @BeforeMethod
    public void initPage() {
        loginPage = new LoginPage(driver);
    }

    /**
     * Scenario 1: Successful Login
     * Verifies that valid credentials authenticate the user and open the dashboard.
     */
    @Test(description = "Verify user can log in successfully with valid credentials")
    public void testSuccessfulLogin() {
        loginPage.login(VALID_USERNAME, VALID_PASSWORD);

        Assert.assertTrue(loginPage.isDashboardDisplayed(), "Dashboard should be visible upon valid login.");
        Assert.assertTrue(loginPage.getWelcomeText().contains(VALID_USERNAME),
                "Welcome text should contain the authenticated username.");
        Assert.assertFalse(loginPage.isLoginFormDisplayed(), "Login form should be hidden after authentication.");
    }

    /**
     * Scenario 2: Invalid Password
     * Verifies that entering a valid username with an incorrect password fails with an error.
     */
    @Test(description = "Verify error message when logging in with invalid password")
    public void testInvalidPassword() {
        loginPage.login(VALID_USERNAME, INVALID_PASSWORD);

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed.");
        Assert.assertEquals(loginPage.getErrorMessage(), ERR_INVALID_CREDENTIALS,
                "Error text should match expected invalid credentials message.");
        Assert.assertFalse(loginPage.isDashboardDisplayed(), "Dashboard should not be visible on failed login.");
    }

    /**
     * Scenario 3: Invalid Username
     * Verifies that entering an unregistered username fails with an error.
     */
    @Test(description = "Verify error message when logging in with invalid username")
    public void testInvalidUsername() {
        loginPage.login(INVALID_USERNAME, VALID_PASSWORD);

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed.");
        Assert.assertEquals(loginPage.getErrorMessage(), ERR_INVALID_CREDENTIALS,
                "Error text should match expected invalid credentials message.");
        Assert.assertFalse(loginPage.isDashboardDisplayed(), "Dashboard should not be visible on failed login.");
    }

    /**
     * Scenario 4: Empty Username
     * Verifies client-side validation when username is left blank.
     */
    @Test(description = "Verify validation error when username field is empty")
    public void testEmptyUsername() {
        loginPage.login("", VALID_PASSWORD);

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed.");
        Assert.assertEquals(loginPage.getErrorMessage(), ERR_EMPTY_USERNAME,
                "Error text should state that username is required.");
        Assert.assertFalse(loginPage.isDashboardDisplayed(), "Dashboard should not be visible.");
    }

    /**
     * Scenario 5: Empty Password
     * Verifies client-side validation when password is left blank.
     */
    @Test(description = "Verify validation error when password field is empty")
    public void testEmptyPassword() {
        loginPage.login(VALID_USERNAME, "");

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed.");
        Assert.assertEquals(loginPage.getErrorMessage(), ERR_EMPTY_PASSWORD,
                "Error text should state that password is required.");
        Assert.assertFalse(loginPage.isDashboardDisplayed(), "Dashboard should not be visible.");
    }

    /**
     * Scenario 6: Both Fields Empty
     * Verifies client-side validation when both username and password are submitted empty.
     */
    @Test(description = "Verify validation error when both username and password fields are empty")
    public void testBothFieldsEmpty() {
        loginPage.login("", "");

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed.");
        Assert.assertEquals(loginPage.getErrorMessage(), ERR_BOTH_EMPTY,
                "Error text should request user to fill in all fields.");
        Assert.assertFalse(loginPage.isDashboardDisplayed(), "Dashboard should not be visible.");
    }

    /**
     * Scenario 7: Dashboard Visibility After Login
     * Verifies that all dashboard components (greeting, metrics, logout action) render correctly.
     */
    @Test(description = "Verify dashboard components and stats visibility after successful login")
    public void testDashboardVisibilityAfterLogin() {
        loginPage.login(VALID_USERNAME, VALID_PASSWORD);

        Assert.assertTrue(loginPage.isDashboardDisplayed(), "Dashboard must be displayed.");
        Assert.assertTrue(loginPage.getWelcomeText().contains("Welcome, admin!"),
                "Dashboard greeting should welcome admin.");
        Assert.assertTrue(loginPage.isLogoutButtonDisplayed(), "Logout button should be present on dashboard.");
        Assert.assertEquals(loginPage.getDashboardStatCardsCount(), 4, "Dashboard should display 4 stat cards.");
    }

    /**
     * Scenario 8: Logout Functionality
     * Verifies that clicking the logout button returns to the login screen and displays a success alert.
     */
    @Test(description = "Verify user can log out successfully and returns to login view")
    public void testLogoutFunctionality() {
        // Step 1: Login
        loginPage.login(VALID_USERNAME, VALID_PASSWORD);
        Assert.assertTrue(loginPage.isDashboardDisplayed(), "Dashboard should be visible before logout.");

        // Step 2: Logout
        loginPage.clickLogout();

        // Step 3: Assertions
        Assert.assertFalse(loginPage.isDashboardDisplayed(), "Dashboard should be hidden after logout.");
        Assert.assertTrue(loginPage.isLoginFormDisplayed(), "Login form should reappear after logout.");
        Assert.assertTrue(loginPage.isSuccessMessageDisplayed(), "Logout confirmation message should appear.");
        Assert.assertEquals(loginPage.getSuccessMessage(), MSG_LOGOUT_SUCCESS,
                "Logout message should confirm successful session termination.");
        Assert.assertEquals(loginPage.getUsernameInputValue(), "", "Username field should be cleared after logout.");
        Assert.assertEquals(loginPage.getPasswordInputValue(), "", "Password field should be cleared after logout.");
    }
}

