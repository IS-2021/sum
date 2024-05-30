<script setup lang="ts">
import type { Uuid } from '@/lib/api-model';
import { StarIcon } from 'lucide-vue-next';
import { useFavourites } from '@/components/favourites/useFavourites';
import RestaurantCard from '@/components/restaurants/RestaurantCard.vue';

const { userId } = defineProps<{ userId: Uuid }>();

const { favourites, hasAnyFavourites } = useFavourites({
  userId,
});
</script>

<template>
  <h1 class="font-bold text-2xl mb-8">Your Favourite Restaurants</h1>

  <div v-if="!hasAnyFavourites">
    <p>
      You don't have any favourite restaurants yet. Click on the
      <StarIcon class="inline-block" /> next to the restaurant to make it appear here.
    </p>
  </div>

  <div class="grid md:grid-cols-2 lg:grid-cols-3 gap-4 mb-8" v-if="favourites">
    <RestaurantCard
      v-for="restaurant in favourites.slice(0, 3)"
      :key="restaurant.id"
      :id="restaurant.id"
      :image-src="restaurant.imageUrl"
      :name="restaurant.name"
      :hours="restaurant.hours"
      :likes="restaurant.likesCount"
      :dislikes="restaurant.dislikesCount"
    />
  </div>
</template>
