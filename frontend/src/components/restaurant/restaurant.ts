import type { Uuid } from '@/lib/api-model';
import { unref, computed } from 'vue';

import { useGetRestaurantsId } from '@/lib/api/restaurants/restaurants';
import { useGetMeals } from '@/lib/api/meals/meals';
import { useGetIngredients } from '@/lib/api/ingredients/ingredients';

export function getRestaurant(id: Uuid) {
  const { data, isPending: areRestaurantsLoading } = useGetRestaurantsId(id);
  return {
    restaurant: computed(() => unref(data)?.data),
    areRestaurantsLoading: areRestaurantsLoading,
  };
}

export function getMeals(restaurantId: Uuid) {
  const { data, isPending: areMealsLoading } = useGetMeals({
    restaurantId: restaurantId,
  });
  return {
    meals: computed(() => unref(data)?.data),
    areMealsLoading: areMealsLoading,
  };
}

export function getIngredients(restaurantId: Uuid) {
  const { data } = useGetIngredients({ restaurantId: restaurantId });
  return {
    ingredients: computed(() => unref(data)?.data),
  };
}
