import { UserInputDTO } from "../lib/api-model";
import { faker } from "../lib/faker";
import { getAuth } from "../lib/api/auth/auth";
import { logEvent } from "../lib/logger";
import { fakePassword, fakePhoneNumber } from "./helpers";

export async function addRestaurantAccount(restaurantName: string) {
  const normalizedUsername = restaurantName.toLowerCase().replace(/\s/g, "_");

  const restaurantAccountInput: UserInputDTO = {
    phoneNumber: fakePhoneNumber(),
    email: `${normalizedUsername}@example.com`,
    password: fakePassword(),
    firstName: faker.person.firstName(),
    secondName: faker.person.lastName(),
    username: normalizedUsername,
    role: "ROLE_RESTAURANT",
  };

  const { data: restaurant, status } = await getAuth().postAuthRegister(restaurantAccountInput);

  if (status === 201) {
    logEvent({
      msg: "Restaurant account created",
      data: restaurant,
    });
  } else {
    logEvent({
      msg: "Failed to create restaurant account",
      data: restaurant,
    });
  }

  return restaurant;
}
