<script setup lang="ts">
import type { BookingDTO } from '@/lib/api-model';
import { useGetMealsId } from '@/lib/api/meals/meals';

import { formatDate } from '@/lib/formatters';

import ActiveBookingRestaurantInfo from '@/components/bookings/ActiveBookingRestaurantInfo.vue';
import { computed, unref } from 'vue';

const props = defineProps<{
  activeBooking: BookingDTO;
}>();

const { data } = useGetMealsId(props.activeBooking.mealId);
const meal = computed(() => unref(data)?.data);
</script>

<template>
  <ul v-if="meal" class="space-y-2">
    <li>
      <p>Meal: {{ meal.name }}</p>
    </li>
    <li>
      <p>
        Order time:
        {{ formatDate(activeBooking.orderedTimestamp) }}
      </p>
    </li>
    <li>
      <p>
        Pick-up deadline:
        {{ formatDate(activeBooking.deadlinePickUpTimestamp) }}
      </p>
    </li>
    <ActiveBookingRestaurantInfo :restaurantId="meal.restaurantId" />
  </ul>
</template>
