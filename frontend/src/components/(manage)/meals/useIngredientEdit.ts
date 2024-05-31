import { computed, ref } from 'vue';
import type { IngredientDTO } from '@/lib/api-model';

export function useIngredientEdit(mealIngredients?: IngredientDTO[]) {
  const pickedIngredients = ref<IngredientDTO[]>(mealIngredients ?? []);
  const pickedIngredientsIds = computed(() => {
    const ids = pickedIngredients.value.map((i) => i.ingredientId);
    return new Set(ids);
  });

  function isPicked(ingredient: string) {
    return pickedIngredientsIds.value.has(ingredient);
  }

  function toggleIngredientPick(ingredient: IngredientDTO) {
    if (pickedIngredientsIds.value.has(ingredient.ingredientId)) {
      pickedIngredients.value = pickedIngredients.value.filter(
        (i) => i.ingredientId !== ingredient.ingredientId,
      );
    } else {
      pickedIngredients.value = [...pickedIngredients.value, ingredient];
    }
  }

  return {
    pickedIngredients,
    isPicked,
    toggleIngredientPick,
  };
}
