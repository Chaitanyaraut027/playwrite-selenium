const { test, expect } = require('@playwright/test');

/**
 * Playwright API Testing Suite
 * Demonstrates API automation using Playwright's native APIRequestContext.
 * Targets the public JSONPlaceholder REST API (https://jsonplaceholder.typicode.com).
 * Covers:
 * - GET single resource & query parameter filtering
 * - POST resource creation
 * - Status code assertions (200 OK, 201 Created, 404 Not Found)
 * - JSON schema and payload integrity validation
 * - Negative test scenarios
 */

const BASE_API_URL = 'https://jsonplaceholder.typicode.com';

test.describe('REST API Automation - JSONPlaceholder', () => {

  test('API-01: GET /posts/1 - Retrieve post details successfully', async ({ request }) => {
    const response = await request.get(`${BASE_API_URL}/posts/1`);

    // Status assertion
    expect(response.status()).toBe(200);
    expect(response.ok()).toBeTruthy();

    // Header assertion
    const headers = response.headers();
    expect(headers['content-type']).toContain('application/json');

    // Body validation
    const data = await response.json();
    expect(data).toHaveProperty('id', 1);
    expect(data).toHaveProperty('userId', 1);
    expect(typeof data.title).toBe('string');
    expect(typeof data.body).toBe('string');
    expect(data.title.length).toBeGreaterThan(0);
  });

  test('API-02: GET /posts?userId=1 - Filter posts by query parameter', async ({ request }) => {
    const response = await request.get(`${BASE_API_URL}/posts`, {
      params: {
        userId: 1
      }
    });

    expect(response.status()).toBe(200);
    const posts = await response.json();

    // Verify response is a non-empty array
    expect(Array.isArray(posts)).toBeTruthy();
    expect(posts.length).toBeGreaterThan(0);

    // Verify every post belongs to userId 1
    posts.forEach(post => {
      expect(post.userId).toBe(1);
      expect(post).toHaveProperty('id');
      expect(post).toHaveProperty('title');
    });
  });

  test('API-03: POST /posts - Create a new post resource', async ({ request }) => {
    const payload = {
      title: 'QA Automation in Playwright',
      body: 'Demonstrating REST API validation with Playwright APIRequestContext for Techdome.',
      userId: 1
    };

    const response = await request.post(`${BASE_API_URL}/posts`, {
      data: payload,
      headers: {
        'Content-type': 'application/json; charset=UTF-8'
      }
    });

    // 201 Created status check
    expect(response.status()).toBe(201);

    const createdPost = await response.json();
    // Validate returned resource contains payload values and generated ID
    expect(createdPost.title).toBe(payload.title);
    expect(createdPost.body).toBe(payload.body);
    expect(createdPost.userId).toBe(payload.userId);
    expect(createdPost).toHaveProperty('id');
  });

  test('API-04: Negative Test - GET non-existent resource returns 404', async ({ request }) => {
    const response = await request.get(`${BASE_API_URL}/posts/99999`);

    // Verify 404 status code
    expect(response.status()).toBe(404);
    expect(response.ok()).toBeFalsy();

    // JSONPlaceholder returns empty object for 404
    const body = await response.json();
    expect(body).toEqual({});
  });

});

