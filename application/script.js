/**
 * Techdome QA Automation Portfolio - Application Logic
 * Client-side script handling authentication, validation, dashboard display, and logout.
 */

(function () {
  'use strict';

  // Valid test credentials
  const VALID_CREDENTIALS = {
    username: 'admin',
    password: 'admin123'
  };

  // DOM Elements
  const loginSection = document.getElementById('login-section');
  const loginForm = document.getElementById('login-form');
  const usernameInput = document.getElementById('username');
  const passwordInput = document.getElementById('password');
  const loginBtn = document.getElementById('login-btn');
  const errorMessage = document.getElementById('error-message');
  const successMessage = document.getElementById('success-message');
  const usernameError = document.getElementById('username-error');
  const passwordError = document.getElementById('password-error');

  const dashboardSection = document.getElementById('dashboard');
  const displayUsername = document.getElementById('display-username');
  const logoutBtn = document.getElementById('logout-btn');

  // Helper functions for alerts and errors
  function showError(message) {
    if (errorMessage) {
      errorMessage.textContent = message;
      errorMessage.style.display = 'block';
    }
  }

  function hideError() {
    if (errorMessage) {
      errorMessage.textContent = '';
      errorMessage.style.display = 'none';
    }
  }

  function showSuccess(message) {
    if (successMessage) {
      successMessage.textContent = message;
      successMessage.style.display = 'block';
    }
  }

  function hideSuccess() {
    if (successMessage) {
      successMessage.textContent = '';
      successMessage.style.display = 'none';
    }
  }

  function clearFieldErrors() {
    usernameInput.classList.remove('input-invalid');
    passwordInput.classList.remove('input-invalid');
    if (usernameError) {
      usernameError.textContent = '';
      usernameError.style.display = 'none';
    }
    if (passwordError) {
      passwordError.textContent = '';
      passwordError.style.display = 'none';
    }
  }

  function resetForm() {
    loginForm.reset();
    clearFieldErrors();
  }

  // Handle Login submission
  function handleLogin(event) {
    event.preventDefault();

    hideError();
    hideSuccess();
    clearFieldErrors();

    const username = usernameInput.value.trim();
    const password = passwordInput.value;

    // Validation scenarios
    if (!username && !password) {
      showError('Please fill in all fields.');
      usernameInput.classList.add('input-invalid');
      passwordInput.classList.add('input-invalid');
      return;
    }

    if (!username) {
      showError('Username is required.');
      usernameInput.classList.add('input-invalid');
      if (usernameError) {
        usernameError.textContent = 'Username is required.';
        usernameError.style.display = 'block';
      }
      return;
    }

    if (!password) {
      showError('Password is required.');
      passwordInput.classList.add('input-invalid');
      if (passwordError) {
        passwordError.textContent = 'Password is required.';
        passwordError.style.display = 'block';
      }
      return;
    }

    // Credential check
    if (username === VALID_CREDENTIALS.username && password === VALID_CREDENTIALS.password) {
      // Successful Login
      loginSection.style.display = 'none';
      dashboardSection.style.display = 'block';
      if (displayUsername) {
        displayUsername.textContent = username;
      }
      resetForm();
    } else {
      // Invalid credentials
      showError('Invalid username or password.');
    }
  }

  // Handle Logout
  function handleLogout() {
    dashboardSection.style.display = 'none';
    loginSection.style.display = 'block';
    resetForm();
    hideError();
    showSuccess('You have been logged out successfully.');
  }

  // Attach Event Listeners
  if (loginForm) {
    loginForm.addEventListener('submit', handleLogin);
  }

  if (logoutBtn) {
    logoutBtn.addEventListener('click', handleLogout);
  }

  // Clear errors when typing
  usernameInput.addEventListener('input', function () {
    if (usernameInput.classList.contains('input-invalid')) {
      usernameInput.classList.remove('input-invalid');
      if (usernameError) usernameError.style.display = 'none';
    }
  });

  passwordInput.addEventListener('input', function () {
    if (passwordInput.classList.contains('input-invalid')) {
      passwordInput.classList.remove('input-invalid');
      if (passwordError) passwordError.style.display = 'none';
    }
  });
})();

