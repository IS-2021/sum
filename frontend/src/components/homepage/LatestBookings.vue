<script setup lang="ts">
import type { BookingDTO } from '@/lib/api-model';
import { getMeal } from '../restaurant/restaurant';
import { getRestaurant } from '../restaurant/restaurant';

import { AspectRatio } from '@/components/ui/aspect-ratio';

const props = defineProps<{
  bookings: BookingDTO[];
}>();

function getBookingRestaurant(booking: BookingDTO) {
  const mealId = booking.mealId;
  const meal = getMeal(mealId);

  if (meal) {
    const restaurantId = meal.restaurantId;
    const restaurant = getRestaurant(restaurantId).restaurant;
    return restaurant;
  }
}
</script>

<template>
  <div v-for="booking in props.bookings.slice(0, 3)" v-bind:key="booking.id">
    <h1 class="font-bold text-2xl mb-8">Last orders</h1>
    <article
      class="group bg-neutral-200 text-neutral-900 rounded text-lg space-y-3 p-4 h-full hover:shadow transition-shadow"
    >
      <AspectRatio :ratio="16 / 9" class="overflow-clip rounded-md">
        <img
          :src="getBookingRestaurant(booking)?.value?.imageUrl"
          :alt="getBookingRestaurant(booking)?.value?.name + ' image'"
          class="object-cover w-full h-full group-hover:scale-105 transition-transform"
        />
      </AspectRatio>

      <header class="flex justify-between items-center">
        <p class="font-bold">{{ getBookingRestaurant(booking)?.value?.name }}</p>
      </header>
    </article>
  </div>
</template>
