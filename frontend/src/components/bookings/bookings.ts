import type { Uuid } from '@/lib/api-model';
import { useGetMealsId } from '@/lib/api/meals/meals';
import { useGetRestaurantsId } from '@/lib/api/restaurants/restaurants';
import { unref } from 'vue';

export function getMeal(mealId: Uuid) {
  const { data } = useGetMealsId(mealId);
  const meal = unref(data)?.data;
  return meal;
}

export function getRestaurant(mealId: Uuid) {
  const meal = getMeal(mealId);
  const restaurantId = meal?.restaurantId;
  if (restaurantId) {
    const { data } = useGetRestaurantsId(restaurantId);
    return unref(data)?.data;
  }
}
