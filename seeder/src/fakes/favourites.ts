import { Uuid } from "../lib/api-model";
import { getFavourites } from "../lib/api/favourites/favourites";
import { logEvent } from "../lib/logger";

export async function addRestaurantToUserFavourites(userId: Uuid, restaurantId: Uuid) {
  const { status, data } = await getFavourites().postUsersIdFavourites(userId, {
    restaurantId,
  });

  if (status === 200) {
    logEvent({
      msg: `Added restaurant to user favourites`,
    });
  } else {
    logEvent({
      msg: "Failed to add restaurant to favourites",
      data,
    });
  }
}
