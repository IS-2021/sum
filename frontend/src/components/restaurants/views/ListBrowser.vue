<script setup lang="ts">
import RestaurantCard from '@/components/restaurants/RestaurantCard.vue';
import type { RestaurantDTO } from '@/lib/api-model';

const props = defineProps<{
  restaurants: RestaurantDTO[];
}>();
</script>

<template>
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
</template>
