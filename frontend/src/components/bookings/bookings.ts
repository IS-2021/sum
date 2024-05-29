import type { Uuid } from '@/lib/api-model';
import { useGetBookingsActive } from '@/lib/api/bookings/bookings';
import { useGetMealsId } from '@/lib/api/meals/meals';
import { useGetRestaurantsId } from '@/lib/api/restaurants/restaurants';
import { unref } from 'vue';

export function getMeal(mealId: Uuid) {
  const { data } = useGetMealsId(mealId);
  const meal = unref(data)?.data;
  return meal;
}

export function getRestaurant(restaurantId: Uuid) {
  const { data } = useGetRestaurantsId(restaurantId);
  return unref(data)?.data;
}

export function getActiveBooking(userId: Uuid) {
  const { data } = useGetBookingsActive({ userId: userId });
  const activeBooking = unref(data)?.data;
  return activeBooking;
}
