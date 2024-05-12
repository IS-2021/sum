import { IngredientInputDTO, Uuid } from "src/lib/api-model";
import { getIngredients } from "src/lib/api/ingredients/ingredients";
import { logEvent } from "src/lib/logger";

export async function addIngredient(mealId: Uuid, name: string, type: string) {
  const ingredientInput: IngredientInputDTO = {
    name: name,
    type: type,
  };

  const { data, status } = await getIngredients().postIngredients(ingredientInput, {
    mealId: mealId,
  });

  if (status === 200) {
    logEvent({
      msg: "Ingredient created",
      data: data,
    });
  } else {
    logEvent({
      msg: "Failed to create ingredient",
      data: {
        ingredientInput,
        data,
      },
    });
  }

  return data;
}
