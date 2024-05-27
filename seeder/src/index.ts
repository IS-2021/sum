import axios from "axios";
import { activateRestaurant, addRestaurant } from "./fakes/restaurants";
import { addRandomImageToRestaurant } from "./fakes/images";
import { addRestaurantToUserFavourites } from "./fakes/favourites";
import { getAddress } from "./fakes/address";
import { addRestaurantAccount } from "./fakes/users";
import { restaurants } from "./seedData";

axios.defaults.validateStatus = () => true;

const userId = "";
const tryAddRestaurantToUserFavourites = false;

async function seed() {
  const promises = restaurants.map(async (restaurantData) => {
    const address = await getAddress(restaurantData.address);
    const restaurantUser = await addRestaurantAccount(restaurantData.name);

    const { id: restaurantId } = await addRestaurant({
      ...restaurantData,
      addressInputDTO: address,
      userId: restaurantUser.id,
    });

    await activateRestaurant(restaurantId);

    await addRandomImageToRestaurant(restaurantId, restaurantData.imageUrl);

    if (tryAddRestaurantToUserFavourites) {
      await addRestaurantToUserFavourites(userId, restaurantId);
    }
  });

  for (const promise of promises) {
    await promise;
  }
}

seed();
