import type { Uuid } from '@/lib/api-model';
import { useGetBookings } from '@/lib/api/bookings/bookings';
import { useGetMealsId } from '@/lib/api/meals/meals';
import { useGetRestaurantsId } from '@/lib/api/restaurants/restaurants';
import { unref } from 'vue';

function getRestaurant(id: Uuid) {
  const { data } = useGetRestaurantsId(id);
  const restaurant = unref(data)?.data;
  return restaurant;
}

function getMeal(id: Uuid) {
  const { data } = useGetMealsId(id);
  const meal = unref(data)?.data;

  return meal;
}

export function getLatestBooking() {
  const { data } = useGetBookings();
  const bookings = unref(data)?.data;

  if (!bookings) {
    throw new Error(`Bookings not found`);
  }

  const index = bookings.length - 1;
  const latestBooking = bookings[index];
  const id = latestBooking.id;

  const latestMeal = getMeal(id);

  if (!latestMeal) {
    throw new Error('No latest meals available');
  }

  const restaurant = getRestaurant(latestMeal.restaurantId);

  return {
    latestMeal: latestMeal,
    restaurant: restaurant,
  };
}
