const { test, expect } = require('@playwright/test');
const path = require('path');
const { pathToFileURL } = require('url');

/**
 * Playwright E2E Test Suite for Techdome QA Application Login
 * Implements 8 positive, negative, and edge test scenarios using standard Playwright locators:
 * getByRole(), getByLabel(), and locator().
 */

// Dynamically resolve application index.html file URL
const APP_URL = pathToFileURL(path.resolve(__dirname, '../../application/index.html')).href;

// Test Data
const CREDENTIALS = {
  valid: {
    username: 'admin',
    password: 'admin123'
  },
  invalid: {
    username: 'wronguser',
    password: 'wrongpassword'
  }
};

const MESSAGES = {
  invalidCredentials: 'Invalid username or password.',
  emptyUsername: 'Username is required.',
  emptyPassword: 'Password is required.',
  bothEmpty: 'Please fill in all fields.',
  logoutSuccess: 'You have been logged out successfully.'
};

test.describe('Techdome QA Portal - Login Automation Suite', () => {

  test.beforeEach(async ({ page }) => {
    // Navigate to the local application before each test
    await page.goto(APP_URL);
    await expect(page).toHaveTitle(/Techdome QA Portal - Login/);
  });

  test.afterEach(async ({ page }, testInfo) => {
    // If test fails, optionally capture an additional screenshot to root screenshots folder
    if (testInfo.status !== testInfo.expectedStatus) {
      const screenshotPath = path.resolve(__dirname, `../../screenshots/FAIL_${testInfo.title.replace(/\s+/g, '_')}.png`);
      await page.screenshot({ path: screenshotPath, fullPage: true });
    }
  });

  test('TC01 - Successful login with valid credentials opens dashboard', async ({ page }) => {
    // Fill credentials using accessible label locators
    await page.getByLabel('Username').fill(CREDENTIALS.valid.username);
    await page.getByLabel('Password').fill(CREDENTIALS.valid.password);

    // Click submit button using role locator
    await page.getByRole('button', { name: 'Sign In' }).click();

    // Verify dashboard is displayed and login form is hidden
    const dashboard = page.locator('#dashboard');
    await expect(dashboard).toBeVisible();
    await expect(page.locator('#login-section')).toBeHidden();

    // Verify welcome greeting
    const welcomeText = page.locator('#welcome-user');
    await expect(welcomeText).toContainText(`Welcome, ${CREDENTIALS.valid.username}!`);
  });

  test('TC02 - Login fails with invalid password', async ({ page }) => {
    await page.getByLabel('Username').fill(CREDENTIALS.valid.username);
    await page.getByLabel('Password').fill(CREDENTIALS.invalid.password);
    await page.getByRole('button', { name: 'Sign In' }).click();

    // Verify error message banner
    const errorMessage = page.locator('#error-message');
    await expect(errorMessage).toBeVisible();
    await expect(errorMessage).toHaveText(MESSAGES.invalidCredentials);

    // Verify dashboard remained hidden
    await expect(page.locator('#dashboard')).toBeHidden();
  });

  test('TC03 - Login fails with invalid username', async ({ page }) => {
    await page.getByLabel('Username').fill(CREDENTIALS.invalid.username);
    await page.getByLabel('Password').fill(CREDENTIALS.valid.password);
    await page.getByRole('button', { name: 'Sign In' }).click();

    const errorMessage = page.locator('#error-message');
    await expect(errorMessage).toBeVisible();
    await expect(errorMessage).toHaveText(MESSAGES.invalidCredentials);

    await expect(page.locator('#dashboard')).toBeHidden();
  });

  test('TC04 - Validation error when username is empty', async ({ page }) => {
    // Leave username empty, fill password
    await page.getByLabel('Password').fill(CREDENTIALS.valid.password);
    await page.getByRole('button', { name: 'Sign In' }).click();

    const errorMessage = page.locator('#error-message');
    await expect(errorMessage).toBeVisible();
    await expect(errorMessage).toHaveText(MESSAGES.emptyUsername);

    await expect(page.locator('#dashboard')).toBeHidden();
  });

  test('TC05 - Validation error when password is empty', async ({ page }) => {
    // Fill username, leave password empty
    await page.getByLabel('Username').fill(CREDENTIALS.valid.username);
    await page.getByRole('button', { name: 'Sign In' }).click();

    const errorMessage = page.locator('#error-message');
    await expect(errorMessage).toBeVisible();
    await expect(errorMessage).toHaveText(MESSAGES.emptyPassword);

    await expect(page.locator('#dashboard')).toBeHidden();
  });

  test('TC06 - Validation error when both fields are empty', async ({ page }) => {
    // Click submit with empty form
    await page.getByRole('button', { name: 'Sign In' }).click();

    const errorMessage = page.locator('#error-message');
    await expect(errorMessage).toBeVisible();
    await expect(errorMessage).toHaveText(MESSAGES.bothEmpty);

    await expect(page.locator('#dashboard')).toBeHidden();
  });

  test('TC07 - Verify dashboard components and metrics visibility after successful login', async ({ page }) => {
    await page.getByLabel('Username').fill(CREDENTIALS.valid.username);
    await page.getByLabel('Password').fill(CREDENTIALS.valid.password);
    await page.getByRole('button', { name: 'Sign In' }).click();

    // Verify Dashboard Header and Components
    await expect(page.locator('#dashboard')).toBeVisible();
    await expect(page.getByRole('heading', { name: 'Automation Dashboard' })).toBeVisible();
    await expect(page.getByRole('button', { name: 'Logout' })).toBeVisible();

    // Verify 4 Stat Cards are rendered
    const statCards = page.locator('.stat-card');
    await expect(statCards).toHaveCount(4);

    // Verify test suite items
    await expect(page.locator('.suite-list')).toBeVisible();
  });

  test('TC08 - Verify user can log out successfully and returns to login view', async ({ page }) => {
    // Step 1: Perform valid login
    await page.getByLabel('Username').fill(CREDENTIALS.valid.username);
    await page.getByLabel('Password').fill(CREDENTIALS.valid.password);
    await page.getByRole('button', { name: 'Sign In' }).click();
    await expect(page.locator('#dashboard')).toBeVisible();

    // Step 2: Click Logout button
    await page.getByRole('button', { name: 'Logout' }).click();

    // Step 3: Assert dashboard is hidden and login form is restored
    await expect(page.locator('#dashboard')).toBeHidden();
    await expect(page.locator('#login-section')).toBeVisible();

    // Verify logout confirmation message
    const successMessage = page.locator('#success-message');
    await expect(successMessage).toBeVisible();
    await expect(successMessage).toHaveText(MESSAGES.logoutSuccess);

    // Verify input fields were reset
    await expect(page.getByLabel('Username')).toHaveValue('');
    await expect(page.getByLabel('Password')).toHaveValue('');
  });

});

