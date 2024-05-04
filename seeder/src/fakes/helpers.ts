import { faker } from "../lib/faker";

export function fakePassword() {
  // return faker.internet.password() + "A1!a";
  return "zaq1@WSX";
}

export function fakePhoneNumber() {
  return faker.string.numeric({
    length: 9,
  });
}
