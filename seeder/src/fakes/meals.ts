import { MealInputDTO, Uuid } from "src/lib/api-model";
import { getMeals } from "src/lib/api/meals/meals";
import { logEvent } from "src/lib/logger";

export async function addMeal(name: string, description: string, restaurantId: Uuid) {
  const mealInput: MealInputDTO = {
    description: description,
    name: name,
    restaurantId: restaurantId,
  };

  const { data, status } = await getMeals().postMeals(mealInput);

  if (status === 200) {
    logEvent({
      msg: "Meal created",
      data: data,
    });
  } else {
    logEvent({
      msg: "Failed to create meal",
      data: {
        mealInput,
        data,
      },
    });
    return null;
  }
  return data;
}
