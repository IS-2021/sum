import axios from "axios";
import { activateRestaurant, addRestaurant } from "./fakes/restaurants";
import { addRandomImageToRestaurant } from "./fakes/images";
import { addRestaurantToUserFavourites } from "./fakes/favourites";

axios.defaults.validateStatus = () => true;

const userId = "";
const tryAddRestaurantToUserFavourites = true;

async function addFakeRestaurant() {
  const restaurant = await addRestaurant();
  await activateRestaurant(restaurant.id);
  await addRandomImageToRestaurant(restaurant.id);

  if (tryAddRestaurantToUserFavourites) {
    await addRestaurantToUserFavourites(userId, restaurant.id);
  }
}

addFakeRestaurant();
