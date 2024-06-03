<script setup lang="ts">
import StatsCard from '@/components/(manage)/dashboard/StatsCard.vue';
import { useUserRating } from '@/components/user-rating/useUserRating';
import type { BookingDTO, RestaurantDTO } from '@/lib/api-model';
import { CookingPotIcon, ThumbsUpIcon } from 'lucide-vue-next';
import RestaurantStatus from '@/components/(manage)/common/RestaurantStatus.vue';

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
    <h1 class="mb-2 text-2xl font-semibold tracking-tight">
      {{ restaurant.name }}
    </h1>
    <RestaurantStatus :status="restaurant.status" />
  </div>

  <div class="mb-8 flex flex-col sm:flex-row sm:gap-6">
    <StatsCard measure="Positive ratings" :value="ratingPercentage">
      <ThumbsUpIcon class="text-primary sm:h-9 sm:w-9" />
    </StatsCard>
    <StatsCard v-if="savedMeals" measure="Meals saved" :value="savedMeals.length.toString()">
      <CookingPotIcon class="text-primary sm:h-9 sm:w-9" />
    </StatsCard>
  </div>
</template>
