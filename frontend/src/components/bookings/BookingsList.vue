<script setup lang="ts">
import type { BookingDTO } from '@/lib/api-model';

import BookingItem from '@/components/bookings/BookingItem.vue';

const props = defineProps<{
  bookings: BookingDTO[];
  username: string;
}>();
</script>

<template>
  <p v-if="!props.bookings || props.bookings.length === 0">
    You don't have any bookings yet. Choose your favourite restaurant and book your meal to make it
    appear here.
  </p>

  <BookingItem
    v-for="booking in bookings"
    v-bind:key="booking.id"
    :booking="booking"
    :username="props.username"
  />
</template>
