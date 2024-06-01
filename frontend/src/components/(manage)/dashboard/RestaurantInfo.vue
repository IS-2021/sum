<script setup lang="ts">
import StatsCard from '@/components/(manage)/dashboard/StatsCard.vue';
import { useUserRating } from '@/components/user-rating/useUserRating';
import type { BookingDTO, RestaurantDTO } from '@/lib/api-model';
import { CookingPotIcon, ThumbsUpIcon } from 'lucide-vue-next';

const props = defineProps<{
  restaurant: RestaurantDTO;
  bookings: BookingDTO[];
}>();

const savedMeals = props.bookings.filter((booking) => booking.status === 'PickedUp');

const { totalRatings, ratingPercentage } = useUserRating(
  props.restaurant.likesCount,
  props.restaurant.dislikesCount,
);
</script>

<template>
  <div v-if="restaurant" class="mb-10">
    <h1 class="text-2xl font-semibold tracking-tight mb-2">
      {{ restaurant.name }}
    </h1>
    <p class="text-lg tracking-tight">Current status: {{ restaurant.status }}</p>
  </div>

  <div class="flex sm:gap-6 mb-8 flex-col sm:flex-row">
    <StatsCard measure="Positive ratings" :value="ratingPercentage">
      <ThumbsUpIcon class="sm:w-9 sm:h-9 text-primary" />
    </StatsCard>
    <StatsCard v-if="savedMeals" measure="Meals saved" :value="savedMeals.length.toString()">
      <CookingPotIcon class="sm:w-9 sm:h-9 text-primary" />
    </StatsCard>
  </div>
</template>
