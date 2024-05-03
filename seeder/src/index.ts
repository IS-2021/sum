import axios from "axios";
import { activateRestaurant, addRestaurant } from "./fakes/restaurants";

axios.defaults.validateStatus = () => true;

async function addFakeRestaurant() {
  const restaurant = await addRestaurant();
  await activateRestaurant(restaurant.id);
}

addFakeRestaurant();
