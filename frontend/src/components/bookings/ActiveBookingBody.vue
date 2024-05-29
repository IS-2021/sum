<script setup lang="ts">
import type { BookingDTO } from '@/lib/api-model';
import { getMealsId } from '@/lib/api/meals/meals';

import { format } from 'date-fns';

import ActiveBookingRestaurantInfo from '@/components/bookings/ActiveBookingRestaurantInfo.vue';

const props = defineProps<{
  activeBooking: BookingDTO;
}>();

const meal = (await getMealsId(props.activeBooking.mealId)).data;
</script>

<template>
  <ul v-if="meal" class="space-y-2">
    <li>
      <p>Meal: {{ meal.name }}</p>
    </li>
    <li>
      <p>
        Order time:
        {{ format(new Date(activeBooking.orderedTimestamp), "MM/dd/yyyy HH':'mm':'ss") }}
      </p>
    </li>
    <li>
      <p>
        Pick-up deadline:
        {{ format(new Date(activeBooking.deadlinePickUpTimestamp), "MM/dd/yyyy HH':'mm':'ss") }}
      </p>
    </li>
    <ActiveBookingRestaurantInfo :restaurantId="meal.restaurantId" />
  </ul>
</template>
