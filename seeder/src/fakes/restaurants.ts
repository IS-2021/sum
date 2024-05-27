import { RestaurantInputDTO, Uuid } from "../lib/api-model";
import { getRestaurants } from "../lib/api/restaurants/restaurants";
import { logEvent } from "../lib/logger";
import { getAdminRestaurants } from "../lib/api/admin-restaurants/admin-restaurants";
import { fakePhoneNumber } from "./helpers";

export async function addRestaurant(restaurantInput: RestaurantInputDTO) {
  const { data, status } = await getRestaurants().postRestaurants({
    ...restaurantInput,
    phoneNumber: fakePhoneNumber(),
  });

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
