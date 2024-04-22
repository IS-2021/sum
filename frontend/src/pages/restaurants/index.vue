<script setup lang="ts">
import { useHead } from '@unhead/vue';
import RestaurantCard from '@/components/restaurants/RestaurantCard.vue';
import { getGetRestaurantsResponseMock } from '@/lib/api/restaurants/restaurants.msw';

useHead({
  title: 'Restaurants',
});

const restaurants = getGetRestaurantsResponseMock();

console.log(restaurants);
</script>

<template>
  <div class="w-full sm:max-w-screen-sm md:max-w-screen-md lg:max-w-screen-xl mx-auto px-4">
    <h1 class="font-bold text-2xl mb-8">Restauracje w mieście: Łódź</h1>

    <div class="grid sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
      <RestaurantCard
        v-for="restaurant in restaurants"
        :key="restaurant.id"
        image-src="https://media-cdn.tripadvisor.com/media/photo-s/1a/37/c1/76/crispy-kimchi-roll-tunczyk.jpg"
        :name="restaurant.name"
        :rating="95"
        opening-hours="12:00"
        closing-hours="22:00"
      />
    </div>
  </div>
</template>
