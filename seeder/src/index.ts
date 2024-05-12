import axios from "axios";
import { activateRestaurant, addRestaurant } from "./fakes/restaurants";
import { addRandomImageToRestaurant } from "./fakes/images";
import { addRestaurantToUserFavourites } from "./fakes/favourites";
import { addMeal } from "./fakes/meals";
import { addIngredient } from "./fakes/ingredient";

axios.defaults.validateStatus = () => true;

const userId = "";
const tryAddRestaurantToUserFavourites = true;

async function addFakeRestaurant() {
  const restaurant = await addRestaurant();
  await activateRestaurant(restaurant.id);
  await addRandomImageToRestaurant(restaurant.id);

  const margerita = await addMeal("Margherita", "Pizza Margherita", restaurant.id);
  const funghi = await addMeal("Fungi", "Pizza Fungi", restaurant.id);
  const capriciosa = await addMeal("Capriciosa", "Pizza Capriciosa", restaurant.id);

  await addIngredient(margerita.mealId, "Mozzarella", "Cheese");
  await addIngredient(margerita.mealId, "Oregano", "Spices");
  await addIngredient(margerita.mealId, "Tomato", "Vegetables");

  await addIngredient(funghi.mealId, "Mozzarella", "Cheese");
  await addIngredient(funghi.mealId, "Tomato", "Vegetables");
  await addIngredient(funghi.mealId, "Champignon", "Mushroom");

  await addIngredient(capriciosa.mealId, "Mozzarella", "Cheese");
  await addIngredient(capriciosa.mealId, "Ham", "Meat");
  await addIngredient(capriciosa.mealId, "Tomato", "Vegetables");

  if (tryAddRestaurantToUserFavourites) {
    await addRestaurantToUserFavourites(userId, restaurant.id);
  }
}

addFakeRestaurant();
