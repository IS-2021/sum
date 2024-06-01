<script setup lang="ts">
import { useUser } from '@/composables/useUser';
import { useGetAdminRestaurants } from '@/lib/api/admin-restaurants/admin-restaurants';
import { computed, type ComputedRef } from 'vue';
import type { RestaurantDTO } from '@/lib/api-model';
import RestaurantCard from '@/components/(admin)/restaurants/RestaurantCard.vue';

const { user, isLoaded } = useUser();

const { data } = useGetAdminRestaurants(
  {},
  {
    query: {
      enabled: isLoaded,
    },
  },
);
const restaurants: ComputedRef<RestaurantDTO[]> = computed(() => {
  if (!data.value) {
    return [];
  }

  return data.value.data as RestaurantDTO[];
});
</script>

<template>
  <h1 class="text-2xl font-semibold tracking-tight mb-10">Restaurants</h1>

  <div v-if="!user || !isLoaded">loading...</div>

  <div
    v-else-if="restaurants"
    class="max-w-screen-xl grid grid-cols-1 md:grid-cols-2 lg:grid-cols-1 xl:grid-cols-2 gap-3 mb-10"
  >
    <RestaurantCard
      v-for="restaurant in restaurants"
      :key="restaurant.id"
      :restaurant="restaurant"
    />
  </div>
</template>
