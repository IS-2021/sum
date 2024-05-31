import type { IngredientDTO, MealInputDTO, Uuid } from '@/lib/api-model';
import { deleteMealsId, postMeals } from '@/lib/api/meals/meals';
import { postIngredients } from '@/lib/api/ingredients/ingredients';

export async function createMeal(meal: MealInputDTO) {
  return postMeals({
    name: meal.name,
    description: meal.description,
    restaurantId: meal.restaurantId,
  });
}

export async function addIngredientsToMeal(
  mealId: Uuid,
  ingredients: IngredientDTO[],
  restaurantId: Uuid,
) {
  const promiseList = ingredients.map((ingredient) => {
    postIngredients({ name: ingredient.name, type: ingredient.type }, { mealId, restaurantId });
  });
  return Promise.all(promiseList);
}

export async function deleteMeal(mealId: Uuid) {
  return deleteMealsId(mealId);
}
