import axios from "axios";
import { activateRestaurant, addRestaurant } from "./fakes/restaurants";
import { addRandomImageToRestaurant } from "./fakes/images";
import { addRestaurantToUserFavourites } from "./fakes/favourites";
import { getAddress } from "./fakes/address";
import { addRestaurantAccount } from "./fakes/users";
import { restaurants } from "./seedData";
import { Mutex } from "async-mutex";
import { addMeal } from "./fakes/meals";
import { addIngredient } from "./fakes/ingredient";

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

    const margerita = await addMeal("Margherita", "Pizza Margherita", restaurantId);
    if (margerita) {
      await addIngredient(margerita.mealId, "Mozzarella", "Cheese");
      await addIngredient(margerita.mealId, "Oregano", "Spices");
      await addIngredient(margerita.mealId, "Tomato", "Vegetables");
    }

    const funghi = await addMeal("Funghi", "Pizza Funghi", restaurantId);
    if (funghi) {
      await addIngredient(funghi.mealId, "Mozzarella", "Cheese");
      await addIngredient(funghi.mealId, "Tomato", "Vegetables");
      await addIngredient(funghi.mealId, "Champignon", "Mushroom");
    }

    const capriciosa = await addMeal("Capriciosa", "Pizza Capriciosa", restaurantId);
    if (capriciosa) {
      await addIngredient(capriciosa.mealId, "Mozzarella", "Cheese");
      await addIngredient(capriciosa.mealId, "Ham", "Meat");
      await addIngredient(capriciosa.mealId, "Tomato", "Vegetables");
    }

    if (tryAddRestaurantToUserFavourites) {
      await addRestaurantToUserFavourites(userId, restaurantId);
    }
  });

  const mutex = new Mutex();

  for (const promise of promises) {
    await mutex.runExclusive(async () => {
      await promise;
    });
  }
}

seed();
