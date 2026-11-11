# QA Automation Testing Strategy

## 1. Executive Summary & Objective

This document outlines the end-to-end Quality Assurance and Test Automation strategy designed for the **Techdome Solutions QA Engineering Portfolio**. The primary objective is to demonstrate industry-standard QA methodologies, automation principles, and robust multi-framework test architecture utilizing **Selenium WebDriver with Java (TestNG)** and **Playwright with JavaScript**.

---

## 2. Testing Levels & Categorization

The test portfolio adheres to the classic **Test Automation Pyramid**, balancing speed, cost, and depth of coverage across different application layers:

```
          / \
         / E2E \         UI Functional Tests (Selenium & Playwright)
        /-------\
       /   API   \       REST API Testing (Playwright APIRequestContext)
      /-----------\
     /  Component  \     Client-side Validation & Unit Assertions
    /---------------\
```

### 2.1. Functional Testing
Functional testing verifies that each software feature functions strictly in accordance with requirements.
- **Login Authentication:** Verifies credential parsing, token/session mock state, and redirection to the dashboard.
- **Form Validation:** Validates required fields, input formats, boundary checks, and error rendering.
- **Session Management:** Validates session termination and state clearing upon logout.

### 2.2. UI Automation
UI automation mimics real user behavior in modern browser environments (Chromium).
- **Selenium WebDriver 4 (Java):** Demonstrates the battle-tested enterprise standard for UI automation. Utilizes the Page Object Model (POM), explicit dynamic waits (`WebDriverWait`), and TestNG listeners for failure diagnostics.
- **Playwright Test (JavaScript):** Demonstrates modern, asynchronous, event-driven web automation featuring auto-waiting, resilient role-based locators (`getByRole`, `getByLabel`), and built-in tracing.

### 2.3. Positive and Negative Testing
- **Positive Testing (Happy Path):** Ensures the application functions properly when provided with valid data (`admin` / `admin123`). Asserts successful authorization and component visibility.
- **Negative Testing:** Exercises the system's defenses and resilience by providing malformed, incomplete, or unauthorized inputs (`wronguser`, `wrongpassword`, empty fields). Asserts that appropriate user-friendly error banners are shown and system security/state is not breached.

### 2.4. Boundary Value Testing
Boundary scenarios check the edges of input validation:
- Single empty username with valid password.
- Single empty password with valid username.
- Both username and password blank.
- Form submission triggers client-side validation preventing blank state execution.

### 2.5. Smoke Testing
The Smoke Suite is a high-priority, fast-executing subset of critical tests executed before full test runs.
- **Scope:** Valid login (`TC_LOGIN_001`), Dashboard element presence (`TC_LOGIN_007`), and Logout (`TC_LOGIN_008`).
- **Goal:** Verify build sanity and core user workflows in under 10 seconds.

### 2.6. Regression Testing
Regression testing ensures that newly added features, refactored code, or bug fixes have not degraded existing functionality.
- **Scope:** Full execution of all 8 UI scenarios across both Selenium and Playwright, combined with API test suites.

### 2.7. End-to-End (E2E) Testing
E2E testing traces the complete user journey from landing page, through authentication, dashboard metrics inspection, and logout.
- **Workflow:** Navigate to Portal &rarr; Verify Login Card &rarr; Authenticate as Admin &rarr; Inspect Dashboard & Stats &rarr; Execute Logout &rarr; Confirm Session Termination.

---

## 3. Test Automation Framework Architecture

### 3.1. Page Object Model (POM) Design Pattern
The Selenium Java framework strictly applies POM:
- **Separation of Concerns:** UI element locators (`By`) and page actions (`enterUsername`, `clickLogin`) reside exclusively in `LoginPage.java`.
- **Test Logic Separation:** Assertions and test flows reside in `LoginTest.java`.
- **Maintainability:** Any UI markup modification requires changing only a single locator in the Page Object without touching test cases.

### 3.2. Synchronization & Wait Strategy
- **Anti-Pattern Avoidance:** Hardcoded `Thread.sleep()` is completely prohibited to avoid flaky and sluggish test runs.
- **Selenium:** Explicit waits (`WebDriverWait` with `ExpectedConditions.visibilityOfElementLocated` and `elementToBeClickable`).
- **Playwright:** Leverages native auto-waiting (waits for elements to be attached, visible, stable, and enabled before clicking or filling).

### 3.3. Test Diagnostics & Failure Reporting
- **Automatic Failure Screenshots:**
  - In Selenium, a custom `TestListener` (`org.testng.ITestListener`) catches `onTestFailure`, captures the current viewport as a PNG, and writes it to `screenshots/FAIL_<TestName>_<timestamp>.png`.
  - In Playwright, `screenshot: 'only-on-failure'` is configured alongside Playwright HTML reports and trace recording.

---

## 4. API Automation Strategy

REST API testing is integrated using Playwright's `APIRequestContext` against `https://jsonplaceholder.typicode.com`:
- Validates HTTP response codes (`200 OK`, `201 Created`, `404 Not Found`).
- Asserts response headers (e.g. `content-type: application/json`).
- Validates JSON payload schema and data types.
- Validates query parameter handling and array filtering.
- Exercises negative paths (requesting missing resources).

---

## 5. Test Execution Strategy & CI/CD Readiness

- **Local Execution:**
  - Headless execution enabled by default for speed, with simple flags for headed mode (`--headed` or `-Dheadless=false`).
  - Cross-platform compatible commands for Windows PowerShell, macOS, and Linux bash.
- **Continuous Integration (CI):**
  - Maven Surefire Plugin runs TestNG suites and generates standard XML/HTML reports (`target/surefire-reports/`).
  - Playwright CLI outputs rich HTML reports (`npx playwright show-report`).

