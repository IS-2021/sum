<script setup lang="ts">
import type { BookingDTO } from '@/lib/api-model';
import { useGetMealsId } from '@/lib/api/meals/meals';
import { computed, unref } from 'vue';

import LatestBookingrestaurantInfo from '@/components/homepage/LatestBookingRestaurantInfo.vue';

const props = defineProps<{
  booking: BookingDTO;
}>();

const { data } = useGetMealsId(props.booking.mealId);
const meal = computed(() => unref(data)?.data);
</script>

<template>
  <article
    class="group bg-neutral-200 text-neutral-900 rounded text-lg space-y-3 p-4 h-full hover:shadow transition-shadow"
  >
    <LatestBookingrestaurantInfo
      v-if="meal"
      :restaurantId="meal.restaurantId"
      :mealName="meal.name"
    />
  </article>
</template>
