<script setup lang="ts">
import { useHead } from '@unhead/vue';
import { useGetRestaurants } from '@/lib/api/restaurants/restaurants';
import { unref } from 'vue';
import RestaurantCard from '@/components/restaurants/RestaurantCard.vue';

useHead({
  title: 'Restaurants',
});

const { data, isPending: areRestaurantsLoading } = useGetRestaurants();
const restaurants = unref(data)?.data;
</script>

<template>
  <template v-if="areRestaurantsLoading">
    <p>Loading...</p>
  </template>
  <template v-else-if="restaurants">
    <div class="w-full sm:max-w-screen-sm md:max-w-screen-md lg:max-w-screen-xl mx-auto px-4">
      <h1 class="font-bold text-2xl mb-8">Restauracje w mieście: Łódź</h1>

      <div class="grid sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
        <RestaurantCard
          v-for="restaurant in restaurants"
          :key="restaurant.id"
          :id="restaurant.id"
          :image-src="restaurant.imageUrl"
          :name="restaurant.name"
          :rating="95"
          opening-hours="12:00"
          closing-hours="22:00"
        />
      </div>
    </div>
  </template>
</template>
