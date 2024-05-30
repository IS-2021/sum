<script setup lang="ts">
import type { BookingDTO } from '@/lib/api-model';

import { formatDate } from '@/lib/formatters';
import { formatAddress } from '@/lib/googleMaps';

const props = defineProps<{
  activeBooking: BookingDTO;
}>();

const meal = props.activeBooking.meal;
const restaurant = props.activeBooking.restaurant;
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
    <li>
      <p>Restaurant: {{ restaurant.name }}</p>
    </li>
    <li>
      <p>Address: {{ formatAddress(restaurant.address) }}</p>
    </li>
    <li>
      <p class="mt-8">Contact: {{ restaurant.phoneNumber }}</p>
    </li>
  </ul>
</template>
