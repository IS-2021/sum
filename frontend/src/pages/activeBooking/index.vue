<route lang="yaml">
meta:
  layout: default
</route>

<script setup lang="ts">
import { useHead } from '@unhead/vue';

import Button from '@/components/ui/button/Button.vue';
import { useUser } from '@/composables/useUser';
import { getBookingsActive } from '@/lib/api/bookings/bookings';
import { ref, watchEffect } from 'vue';
import type { BookingDTO, MealDTO, RestaurantDTO } from '@/lib/api-model';
import { getMealsId } from '@/lib/api/meals/meals';
import { getRestaurantsId } from '@/lib/api/restaurants/restaurants';

useHead({
  title: 'Active Booking - FoodGood',
});

const { user } = useUser();
const activeBooking = ref<BookingDTO | null>(null);
const meal = ref<MealDTO | null>(null);
const restaurant = ref<RestaurantDTO | null>(null);

watchEffect(async () => {
  if (!user.value) {
    return;
  }

  const res = await getBookingsActive({
    userId: user.value.id,
  });

  if (res.status !== 200) {
    throw new Error('Failed to fetch active booking');
  }

  if (res.status === 200 && res.data) {
    activeBooking.value = res.data;

    meal.value = (await getMealsId(activeBooking.value.mealId)).data;
    restaurant.value = (await getRestaurantsId(activeBooking.value.userId)).data;
  }
});
</script>

<template>
  <div class="w-full sm:max-w-screen-sm md:max-w-screen-md lg:max-w-screen-xl mx-auto px-4">
    <h1 class="font-bold text-2xl mb-8 tracking-tight">Active Booking</h1>
    <div class="space-y-5 p-4 bg-neutral-100 border border-neutral-200 w-full">
      <h2 class="font-semibold">Order details</h2>
      <p v-if="!activeBooking">You don't have any active bookings.</p>
      <ul v-if="meal && activeBooking" class="space-y-2">
        <li>
          <p>Meal: {{ meal.name }}</p>
        </li>
        <!--        <li>-->
        <!--          <p>Restaurant: {{ getRestaurant(activeBooking.mealId)?.name }}</p>-->
        <!--        </li>-->
        <!--        <li>-->
        <!--          <p>Address: {{ formatAddress(restaurant.address) }}</p>-->
        <!--        </li>-->
        <li>
          <p>Order time: {{ activeBooking.orderedTimestamp }}</p>
        </li>
        <li>
          <p>Pick-up deadline: {{ activeBooking.deadlinePickUpTimestamp }}</p>
        </li>
        <!--        <li>-->
        <!--          <p class="mt-8">Contact: {{ getRestaurant(activeBooking.mealId)?.phoneNumber }}</p>-->
        <!--        </li>-->
      </ul>
    </div>
    <RouterLink to="/bookings">
      <Button class="mt-8">View all bookings</Button>
    </RouterLink>
  </div>
</template>
