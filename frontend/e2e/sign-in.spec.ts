import { test, expect } from '@playwright/test';
import { faker } from '@faker-js/faker';

test('Sign-in with invalid credentials shows an error', async ({ page }) => {
  await page.goto('http://localhost:5173/sign-in');

  await page.getByPlaceholder('Username').click();
  await page
    .getByPlaceholder('Username')
    .fill(faker.internet.userName() + faker.internet.userName());
  await page.getByPlaceholder('Password').click();
  await page.getByPlaceholder('Password').fill('zaq1@WSX');

  await page.locator('form').getByRole('button', { name: 'Sign in' }).click();

  const errorElement = await page
    .getByText("There's an errorAuthentication Failed. Wrong username or password or both")
    .click();
  expect(errorElement).not.toBeNull();
});
