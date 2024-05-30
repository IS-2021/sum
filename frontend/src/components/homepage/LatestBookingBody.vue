<script setup lang="ts">
import type { BookingDTO } from '@/lib/api-model';

import RestaurantImageComponent from '@/components/homepage/RestaurantImageComponent.vue';

const props = defineProps<{
  booking: BookingDTO;
}>();

const meal = props.booking.meal;
const restaurant = props.booking.restaurant;
</script>

<template>
  <article
    class="group bg-neutral-200 text-neutral-900 rounded text-lg space-y-3 p-4 h-full hover:shadow transition-shadow"
  >
    <article v-if="restaurant">
      <RestaurantImageComponent :imageUrl="restaurant.imageUrl" :restaurantName="restaurant.name" />

      <header class="flex flex-col justify-between items-start">
        <div class="flex">
          <p class="font-bold mr-1">Restaurant:</p>
          <p>{{ restaurant.name }}</p>
        </div>
        <div class="flex">
          <p class="font-bold mr-1">Meal:</p>
          <p>{{ meal.name }}</p>
        </div>
      </header>
    </article>
  </article>
</template>
