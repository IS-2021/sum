<script setup lang="ts">
import { useHead } from '@unhead/vue';
import { useGetRestaurants } from '@/lib/api/restaurants/restaurants';
import { computed, unref } from 'vue';
import RestaurantCard from '@/components/restaurants/RestaurantCard.vue';
import { useUser } from '@/composables/useUser';

useHead({
  title: 'Restaurants - Food Good',
});

const { user } = useUser();
const { data, isPending: areRestaurantsLoading } = useGetRestaurants();
const restaurants = computed(() => unref(data)?.data);
</script>

<template>
  <template v-if="areRestaurantsLoading">
    <p>Loading...</p>
  </template>
  <template v-else-if="restaurants">
    <div class="w-full sm:max-w-screen-sm md:max-w-screen-md lg:max-w-screen-xl mx-auto px-4">
      <h1 class="font-bold text-2xl mb-8 tracking-tight">
        Restaurants {{ user?.address && `in ${user.address.city}` }}
      </h1>

      <div class="grid md:grid-cols-2 lg:grid-cols-3 gap-4" v-if="restaurants.length > 0">
        <RestaurantCard
          v-for="restaurant in restaurants"
          :key="restaurant.id"
          :id="restaurant.id"
          :image-src="restaurant.imageUrl"
          :name="restaurant.name"
          :hours="restaurant.hours"
          :likes="restaurant.likesCount"
          :dislikes="restaurant.dislikesCount"
        />
      </div>
      <div v-else class="flex items-center gap-10 justify-center mt-10">
        <img src="@/assets/images/undraw_no_data_re_kwbl.svg" class="w-20" />
        <div>
          <p class="text-lg tracking-tight mb-2 font-semibold">
            All restaurant menus in your area are empty
          </p>
          <p>Try changing the location in your profile</p>
        </div>
      </div>
    </div>
  </template>
</template>
