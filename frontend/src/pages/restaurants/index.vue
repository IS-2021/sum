<script setup lang="ts">
import { useHead } from '@unhead/vue';
import RestaurantCard from '@/components/restaurants/RestaurantCard.vue';

useHead({
  title: 'Restaurants',
});
</script>

<template>
  <div class="w-full mx-auto px-4">
    <h1 class="font-bold text-2xl mb-10">Restauracje w mieście: Łódź</h1>

    <div class="grid sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
      <RestaurantCard
        name="Tabu Sushi"
        :rating="4.89"
        opening-hours="10:00"
        closing-hours="22:00"
      />
    </div>
  </div>
</template>
