import { test, expect, type Page } from '@playwright/test';

async function createAccount(page: Page) {
  await page.goto('http://localhost:5173/sign-up');

  await page.getByPlaceholder('John').click();
  await page.getByPlaceholder('John').fill('John');
  await page.getByPlaceholder('Cena').click();
  await page.getByPlaceholder('Cena').fill('Cena');
  await page.getByPlaceholder('E-Mail').click();
  await page.getByPlaceholder('E-Mail').fill('johncena@example.com');
  await page.getByPlaceholder('Username').click();
  await page.getByPlaceholder('Username').fill('johncena');
  await page.getByPlaceholder('111222333').click();
  await page.getByPlaceholder('111222333').fill('111222333');
  await page.getByPlaceholder('Password').click();
  await page.getByPlaceholder('Password').fill('zaq1@WSX');

  await page.getByRole('button', { name: 'Sign up' }).click();
}

test('Sign-up with existing account should show an error', async ({ page }) => {
  await createAccount(page);
  await createAccount(page);

  const errorElement = await page
    .getByText("There's an errorUsername: 'johncena' already exists.")
    .click();
  expect(errorElement).not.toBeNull();
});
