# Test Cases Documentation

This document defines the test cases automated within the **Techdome QA Automation Portfolio**. The test suite includes functional UI automation (Selenium Java & Playwright JavaScript) and API automation (Playwright APIRequestContext).

---

## 1. Summary of Test Coverage

| Test Type | Total Cases | Automated in Selenium Java | Automated in Playwright JS | Status |
| :--- | :---: | :---: | :---: | :---: |
| **Positive UI Tests** | 3 | Yes | Yes | PASSED |
| **Negative UI Tests** | 3 | Yes | Yes | PASSED |
| **Boundary / Validation UI Tests** | 2 | Yes | Yes | PASSED |
| **REST API Tests** | 4 | N/A (Playwright Scope) | Yes | PASSED |
| **Total Test Cases** | **12** | **8** | **12** | **100% Passed** |

---

## 2. UI Functional Test Cases (Login & Dashboard)

### TC_LOGIN_001: Successful Login with Valid Credentials
- **Test ID:** `TC_LOGIN_001`
- **Category:** Positive Testing / Happy Path
- **Priority:** High / P1
- **Severity:** Critical
- **Preconditions:**
  1. The application `index.html` is loaded in the browser.
  2. The user is currently on the Sign In view.
- **Test Steps:**
  1. Locate the **Username** field and input `admin`.
  2. Locate the **Password** field and input `admin123`.
  3. Click on the **Sign In** button (`#login-btn`).
- **Test Data:**
  - `Username`: `admin`
  - `Password`: `admin123`
- **Expected Result:**
  - Login section (`#login-section`) is hidden.
  - Dashboard section (`#dashboard`) becomes visible.
  - User greeting displays `Welcome, admin!`.
- **Actual Result:** Dashboard renders immediately with `Welcome, admin!` greeting.
- **Status:** **PASS**

---

### TC_LOGIN_002: Login Attempt with Invalid Password
- **Test ID:** `TC_LOGIN_002`
- **Category:** Negative Testing
- **Priority:** High / P1
- **Severity:** Major
- **Preconditions:**
  1. Application is loaded on the login screen.
- **Test Steps:**
  1. Input `admin` into the Username field.
  2. Input `wrongpassword` into the Password field.
  3. Click **Sign In**.
- **Test Data:**
  - `Username`: `admin`
  - `Password`: `wrongpassword`
- **Expected Result:**
  - Authentication fails.
  - An error alert banner (`#error-message`) is displayed.
  - Alert contains the message: `"Invalid username or password."`.
  - Dashboard remains hidden.
- **Actual Result:** Error alert displays `"Invalid username or password."`. Dashboard remains hidden.
- **Status:** **PASS**

---

### TC_LOGIN_003: Login Attempt with Invalid Username
- **Test ID:** `TC_LOGIN_003`
- **Category:** Negative Testing
- **Priority:** High / P1
- **Severity:** Major
- **Preconditions:**
  1. Application is loaded on the login screen.
- **Test Steps:**
  1. Input `wronguser` into the Username field.
  2. Input `admin123` into the Password field.
  3. Click **Sign In**.
- **Test Data:**
  - `Username`: `wronguser`
  - `Password`: `admin123`
- **Expected Result:**
  - Authentication fails.
  - Error banner displays `"Invalid username or password."`.
  - Dashboard is not visible.
- **Actual Result:** Error alert displays `"Invalid username or password."`.
- **Status:** **PASS**

---

### TC_LOGIN_004: Validation on Empty Username
- **Test ID:** `TC_LOGIN_004`
- **Category:** Boundary / Client-Side Validation
- **Priority:** Medium / P2
- **Severity:** Moderate
- **Preconditions:**
  1. Application is loaded on the login screen.
- **Test Steps:**
  1. Leave Username field empty (null/blank).
  2. Input `admin123` into Password field.
  3. Click **Sign In**.
- **Test Data:**
  - `Username`: `""` (empty)
  - `Password`: `admin123`
- **Expected Result:**
  - Form validation prevents authentication submission.
  - Error banner displays `"Username is required."`.
  - Username field is styled with invalid border highlight.
  - Dashboard is not visible.
- **Actual Result:** `"Username is required."` error displayed and field highlighted.
- **Status:** **PASS**

---

### TC_LOGIN_005: Validation on Empty Password
- **Test ID:** `TC_LOGIN_005`
- **Category:** Boundary / Client-Side Validation
- **Priority:** Medium / P2
- **Severity:** Moderate
- **Preconditions:**
  1. Application is loaded on the login screen.
- **Test Steps:**
  1. Input `admin` into Username field.
  2. Leave Password field empty.
  3. Click **Sign In**.
- **Test Data:**
  - `Username`: `admin`
  - `Password`: `""` (empty)
- **Expected Result:**
  - Form validation blocks authentication.
  - Error banner displays `"Password is required."`.
  - Password field is styled with error indicator.
  - Dashboard is not visible.
- **Actual Result:** `"Password is required."` error displayed.
- **Status:** **PASS**

---

### TC_LOGIN_006: Validation on Both Fields Empty
- **Test ID:** `TC_LOGIN_006`
- **Category:** Boundary / Edge Case
- **Priority:** Medium / P2
- **Severity:** Moderate
- **Preconditions:**
  1. Application is loaded on the login screen.
- **Test Steps:**
  1. Leave Username field empty.
  2. Leave Password field empty.
  3. Click **Sign In**.
- **Test Data:**
  - `Username`: `""`
  - `Password`: `""`
- **Expected Result:**
  - Error banner displays `"Please fill in all fields."`.
  - Both inputs are highlighted in error state.
  - Dashboard is not displayed.
- **Actual Result:** `"Please fill in all fields."` displayed.
- **Status:** **PASS**

---

### TC_LOGIN_007: Dashboard Components and Metrics Visibility
- **Test ID:** `TC_LOGIN_007`
- **Category:** Positive / UI Component Verification
- **Priority:** High / P1
- **Severity:** Minor
- **Preconditions:**
  1. User has successfully authenticated with `admin` / `admin123`.
- **Test Steps:**
  1. Inspect `#dashboard` container visibility.
  2. Verify Dashboard heading: `"Automation Dashboard"`.
  3. Verify Welcome banner contains `"Welcome, admin!"`.
  4. Verify the presence of the 4 metric cards:
     - Automated Tests (16)
     - Frameworks (2)
     - Scenario Coverage (100%)
     - Health Status (PASSING)
  5. Verify the **Logout** button (`#logout-btn`) is displayed and enabled.
- **Test Data:** Valid session credentials (`admin` / `admin123`).
- **Expected Result:** All 5 dashboard UI components and 4 stat cards render correctly.
- **Actual Result:** Dashboard renders completely with all metrics and controls visible.
- **Status:** **PASS**

---

### TC_LOGIN_008: Logout Functionality and State Reset
- **Test ID:** `TC_LOGIN_008`
- **Category:** Positive / State Transition
- **Priority:** High / P1
- **Severity:** Critical
- **Preconditions:**
  1. User is authenticated and active on the Dashboard view.
- **Test Steps:**
  1. Click on the **Logout** button (`#logout-btn`).
- **Test Data:** Active session.
- **Expected Result:**
  - Dashboard section is hidden.
  - Login section (`#login-section`) is restored.
  - Success message banner displays `"You have been logged out successfully."`.
  - Form input fields (`#username` and `#password`) are cleared.
- **Actual Result:** Session cleared, login form redisplayed with confirmation message and blank inputs.
- **Status:** **PASS**

---

## 3. REST API Test Cases (JSONPlaceholder via Playwright APIRequestContext)

### TC_API_001: GET Post by ID (Single Resource)
- **Test ID:** `TC_API_001`
- **Endpoint:** `GET https://jsonplaceholder.typicode.com/posts/1`
- **Category:** Positive API Test
- **Priority:** High / P1
- **Expected Result:**
  - HTTP Status: `200 OK`
  - Header: `content-type` includes `application/json`
  - Body Schema: Contains `id: 1`, `userId: 1`, string `title`, string `body`.
- **Actual Result:** HTTP 200, JSON schema valid, payload matched post ID 1.
- **Status:** **PASS**

---

### TC_API_002: GET Filtered Posts by User Query Parameter
- **Test ID:** `TC_API_002`
- **Endpoint:** `GET https://jsonplaceholder.typicode.com/posts?userId=1`
- **Category:** Positive Query Filtering API Test
- **Priority:** Medium / P2
- **Expected Result:**
  - HTTP Status: `200 OK`
  - Body: JSON array with length > 0
  - Data integrity: Every item has `userId === 1` and contains `id` and `title`.
- **Actual Result:** HTTP 200, array of 10 posts returned, each with `userId: 1`.
- **Status:** **PASS**

---

### TC_API_003: POST Create New Post
- **Test ID:** `TC_API_003`
- **Endpoint:** `POST https://jsonplaceholder.typicode.com/posts`
- **Category:** Positive Mutation API Test
- **Priority:** High / P1
- **Payload:**
  ```json
  {
    "title": "QA Automation in Playwright",
    "body": "Demonstrating REST API validation with Playwright APIRequestContext for Techdome.",
    "userId": 1
  }
  ```
- **Expected Result:**
  - HTTP Status: `201 Created`
  - Body: Contains generated `id: 101` and echoes `title`, `body`, and `userId`.
- **Actual Result:** HTTP 201 Created, returned ID 101 with exact payload attributes.
- **Status:** **PASS**

---

### TC_API_004: Negative Test - GET Non-Existent Resource (404 Not Found)
- **Test ID:** `TC_API_004`
- **Endpoint:** `GET https://jsonplaceholder.typicode.com/posts/99999`
- **Category:** Negative API Test / Error Handling
- **Priority:** Medium / P2
- **Expected Result:**
  - HTTP Status: `404 Not Found`
  - Body: Empty object `{}`
- **Actual Result:** HTTP 404 returned, empty JSON response body verified.
- **Status:** **PASS**

