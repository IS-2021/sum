import axios from "axios";
import { activateRestaurant, addRestaurant } from "./fakes/restaurants";
import { addRandomImageToRestaurant } from "./fakes/images";

axios.defaults.validateStatus = () => true;

async function addFakeRestaurant() {
  const restaurant = await addRestaurant();
  await activateRestaurant(restaurant.id);
  await addRandomImageToRestaurant(restaurant.id);
}

addFakeRestaurant();
