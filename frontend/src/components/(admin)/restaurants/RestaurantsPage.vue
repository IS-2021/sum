<script setup lang="ts">
import { useUser } from '@/composables/useUser';
import { useGetAdminRestaurants } from '@/lib/api/admin-restaurants/admin-restaurants';
import { computed, type ComputedRef } from 'vue';
import type { RestaurantDTO } from '@/lib/api-model';
import RestaurantCard from '@/components/(admin)/restaurants/RestaurantCard.vue';
import SearchInput from '@/components/(admin)/common/SearchInput.vue';
import RestaurantStatusSelect from '@/components/(admin)/common/RestaurantStatusSelect.vue';

const { user, isLoaded } = useUser();

const { data } = useGetAdminRestaurants(
  {},
  {
    query: {
      enabled: isLoaded,
    },
  },
);
const restaurants = computed(() => data.value?.data ?? []);

const searchText = defineModel<string>('searchText', {
  type: String,
  default: '',
});
const statusFilter = defineModel<string>('statusFilter', {
  default: 'all',
});

type FilterableRestaurantProperties = 'name' | 'phoneNumber' | 'id';

const filteredRestaurants = computed(() => {
  const propertiesToFilterBy: FilterableRestaurantProperties[] = ['name', 'phoneNumber', 'id'];

  const filteredByProperty = restaurants.value.filter((restaurant: RestaurantDTO) => {
    return propertiesToFilterBy.some((property) => {
      const restaurantProperty = restaurant[property];
      return restaurantProperty.toLowerCase().includes(searchText.value.toLowerCase());
    });
  });

  return statusFilter.value === 'all'
    ? filteredByProperty
    : filteredByProperty.filter(
        (restaurant: RestaurantDTO) => restaurant.status === statusFilter.value,
      );
});
</script>

<template>
  <div
    class="mb-10 flex max-w-screen-xl flex-col justify-between gap-2 lg:flex-row lg:items-center"
  >
    <h1 class="text-2xl font-semibold tracking-tight">Restaurants</h1>

    <div class="flex flex-wrap gap-2 lg:flex-nowrap">
      <SearchInput v-model="searchText" />
      <RestaurantStatusSelect @update:modelValue="(val) => (statusFilter = val)" />
    </div>
  </div>

  <div v-if="!user || !isLoaded">loading...</div>

  <div
    v-else-if="restaurants"
    class="mb-10 grid max-w-screen-xl grid-cols-1 gap-3 md:grid-cols-2 lg:grid-cols-1 xl:grid-cols-2"
  >
    <RestaurantCard
      v-for="restaurant in filteredRestaurants"
      :key="restaurant.id"
      :restaurant="restaurant"
    />
  </div>
</template>
