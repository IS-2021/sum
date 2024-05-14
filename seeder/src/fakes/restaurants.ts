import { addRestaurantAccount } from "./users";
import { RestaurantInputDTO, Uuid } from "../lib/api-model";
import { faker } from "../lib/faker";
import { getFakeHours } from "./hours";
import { getRestaurants } from "../lib/api/restaurants/restaurants";
import { logEvent } from "../lib/logger";
import { fakePhoneNumber } from "./helpers";
import { getAdminRestaurants } from "../lib/api/admin-restaurants/admin-restaurants";
import { getCityId } from "./cities";

export async function addRestaurant() {
  const restaurantUser = await addRestaurantAccount();

  const cityId = await getCityId("Warszawa");
  if (!cityId) {
    throw new Error("City was not found");
  }

  const restaurantInput: RestaurantInputDTO = {
    name: faker.lorem.words(4),
    addressInputDTO: {
      number: faker.location.buildingNumber(),
      cityId: cityId,
      country: faker.location.country(),
      postalCode: faker.location.zipCode(),
      street: faker.location.street(),
    },
    hours: {
      monday: getFakeHours(),
      tuesday: getFakeHours(),
      wednesday: getFakeHours(),
      thursday: getFakeHours(),
      friday: getFakeHours(),
      saturday: getFakeHours(),
      sunday: getFakeHours(),
    },
    phoneNumber: fakePhoneNumber(),
    userId: restaurantUser.id,
  };

  const { data, status } = await getRestaurants().postRestaurants(restaurantInput);

  if (status === 200) {
    logEvent({
      msg: "Restaurant created",
      data: data,
    });
  } else {
    logEvent({
      msg: "Failed to create restaurant",
      data: {
        restaurantInput,
        data,
      },
    });
  }

  return data;
}

export async function activateRestaurant(restaurantId: Uuid) {
  await getAdminRestaurants().putAdminRestaurantsId(restaurantId);
}
