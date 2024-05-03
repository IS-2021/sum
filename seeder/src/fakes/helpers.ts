import { faker } from "../lib/faker";

export function fakePassword() {
  return faker.internet.password() + "A1!a";
}

export function fakePhoneNumber() {
  return faker.string.numeric({
    length: 9,
  });
}
