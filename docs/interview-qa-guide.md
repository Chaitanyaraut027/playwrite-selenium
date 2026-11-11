# Techdome Solutions - QA Automation Engineer Intern Interview Guide

This guide compiles essential technical concepts, framework comparisons, code explanations, and scenario-based questions tailored specifically for your **Quality Assurance Engineer Intern** interview at **Techdome Solutions**.

---

## 1. Core Framework Comparison: Selenium vs. Playwright

### Q1: What are the fundamental differences between Selenium WebDriver and Playwright?
| Dimension | Selenium WebDriver 4 | Playwright |
| :--- | :--- | :--- |
| **Architecture** | Client-Server over HTTP (W3C WebDriver Standard). | Bi-directional communication over a single WebSocket connection using the Chrome DevTools Protocol (CDP). |
| **Driver Management** | Historically required downloading drivers; Selenium 4 now embeds **Selenium Manager**. | Automatically downloads and pins dedicated browser binaries (`chromium`, `firefox`, `webkit`). |
| **Wait Strategy** | Manual explicit waits (`WebDriverWait`) required to avoid race conditions. | **Automatic waiting** built-in (waits for elements to be attached, visible, stable, enabled, and editable). |
| **Network Interception** | Added in Selenium 4 via BiDi / CDP, but requires explicit instrumentation. | Native, first-class feature (`page.route()`, `page.waitForResponse()`). |
| **Speed & Isolation** | Slower due to HTTP round-trips; browser restart overhead per test. | Extremely fast; lightweight browser contexts allow tests to run with separate cookies and cache in milliseconds. |
| **API Testing** | Requires third-party libraries like REST Assured or Apache HttpClient. | Built-in via `APIRequestContext` (`request` fixture). |

---

## 2. Page Object Model (POM) & Clean Code

### Q2: What is the Page Object Model (POM), and why did you use it in your Selenium suite?
- **Definition:** POM is a design pattern where web pages (or UI components) are represented by dedicated classes. The class contains element locators (`By`) and methods to interact with those elements.
- **Why use it?**
  1. **Separation of Concerns:** Test scripts (`LoginTest.java`) only contain test flows and business assertions; they never contain raw element locators or DOM queries.
  2. **Reduced Maintenance:** If the login button ID changes from `#login-btn` to `#submit-btn`, you only update `LoginPage.java`. None of your 8 test cases will break.
  3. **Reusability & Readability:** Methods like `loginPage.login("admin", "admin123")` make tests self-documenting.

---

## 3. Synchronization & Wait Strategies

### Q3: Why is `Thread.sleep()` considered an anti-pattern in test automation?
- **Unpredictable Execution:** If you write `Thread.sleep(5000)`, the test halts execution for 5 seconds even if the button rendered in 200 milliseconds. Over hundreds of tests, this adds hours of unnecessary delay.
- **False Failures:** If network latency causes an element to take 5.1 seconds, the test fails anyway.
- **The Solution:** Use **explicit dynamic waits** (`WebDriverWait` in Selenium) or **auto-waiting** in Playwright. Explicit waits poll the DOM at 500ms intervals and proceed immediately once the condition (`visibilityOfElementLocated`, `elementToBeClickable`) evaluates to true.

### Q4: Explain Implicit Wait vs. Explicit Wait vs. Fluent Wait in Selenium.
- **Implicit Wait:** A global setting telling the driver to poll the DOM for a maximum duration when finding any element. It does not wait for visibility or clickability, only DOM presence.
- **Explicit Wait:** Applied conditionally to specific elements using `WebDriverWait` and `ExpectedConditions` (e.g., waiting for an alert banner to be visible or a button to be clickable).
- **Fluent Wait:** A customizable explicit wait where you can define the timeout, polling frequency (e.g., every 250ms), and ignore specific exceptions (e.g., `NoSuchElementException`, `StaleElementReferenceException`).

---

## 4. TestNG Testing Framework

### Q5: What is TestNG, and which annotations did you use in your project?
TestNG is a Java testing framework inspired by JUnit, designed for integration and end-to-end testing.
- `@BeforeMethod`: Runs before each test method (used in `BaseTest.java` to instantiate a clean `ChromeDriver` and open the app).
- `@AfterMethod`: Runs after each test method (used to invoke `driver.quit()`, preventing browser zombie processes).
- `@Test`: Marks a method as an automated test case. Attributes like `description` and `priority` document execution intent.
- `ITestListener`: A TestNG interface implemented by `TestListener.java`. It listens to events like `onTestSuccess` and `onTestFailure` to capture automated failure screenshots.

---

## 5. REST API Automation

### Q6: How did you implement API testing in Playwright without third-party libraries?
Playwright provides the native `request` fixture (`APIRequestContext`), which sends HTTP requests directly over Node.js without launching a browser:
```javascript
test('API Test', async ({ request }) => {
  const response = await request.get('https://jsonplaceholder.typicode.com/posts/1');
  expect(response.status()).toBe(200);
  const data = await response.json();
  expect(data.id).toBe(1);
});
```
- **Tests Implemented:**
  1. `GET /posts/1`: Asserts HTTP 200, JSON content-type header, and payload attributes (`id`, `title`, `userId`).
  2. `GET /posts?userId=1`: Asserts query parameter filtering and verifies every item in the array has `userId === 1`.
  3. `POST /posts`: Sends JSON payload, verifies HTTP 201 Created, generated resource ID `101`, and payload echoing.
  4. `GET /posts/99999`: Negative test asserting HTTP 404 Not Found and empty body response.

---

## 6. Software Testing Fundamentals

### Q7: What is the difference between Smoke, Sanity, and Regression Testing?
- **Smoke Testing:** "Build verification testing." Runs a small set of high-priority happy path tests (e.g., login, dashboard render, logout) to ensure the core build is stable enough for deeper testing.
- **Sanity Testing:** A focused subset executed after a bug fix or minor release to verify that specific components work as intended without testing the entire application.
- **Regression Testing:** Complete execution of all test suites (positive, negative, boundary, edge cases) to ensure recent code changes have not broken existing functionality.

### Q8: What are Boundary Value Analysis (BVA) and Equivalence Class Partitioning (ECP)?
- **Equivalence Class Partitioning (ECP):** Grouping inputs into valid and invalid classes where all members of a class are expected to yield the same result. For example, valid username (`admin`) vs. invalid usernames (`wronguser1`, `wronguser2`).
- **Boundary Value Analysis (BVA):** Testing at the boundaries of input domains. In our login application:
  - Empty username + valid password.
  - Valid username + empty password.
  - Both fields empty.

---

## 7. Scenario-Based & Practical QA Questions

### Q9: If a test fails in your automated suite, how do you investigate it?
1. **Check Failure Type:** Determine whether it is an Assertion Failure (functional regression), Locator Error (`NoSuchElementException` / UI changed), or Timeout / Environmental issue.
2. **Review Diagnostics:** Inspect the automated failure screenshot captured in `screenshots/FAIL_*.png` and check console/trace logs.
3. **Reproduce Manually:** Open the application manually in the browser and execute the exact test steps and data.
4. **Log the Defect:** If confirmed as an application bug, log a Jira ticket with Title, Severity, Steps to Reproduce, Expected vs. Actual Result, and attached screenshots/logs.

### Q10: How do you handle flaky tests?
1. **Analyze Root Cause:** Check if the flakiness is caused by race conditions, network latency, animation delays, or shared test state.
2. **Replace Static Delays with Explicit Waits:** Ensure no `Thread.sleep` or rigid timeouts exist.
3. **Ensure Test Isolation:** Each test must start from a clean state (handled via `@BeforeMethod` browser restarts or Playwright isolated browser contexts).
4. **Stabilize Selectors:** Switch from fragile deep XPath (`/html/body/div[2]/form/div[1]/input`) to semantic, user-facing locators (`getByRole`, `getByLabel`, stable IDs).
