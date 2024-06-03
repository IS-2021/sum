<script setup lang="ts">
import type { BookingDTO, Uuid } from '@/lib/api-model';

import BookingItem from '@/components/bookings/BookingItem.vue';
import { Accordion } from '@/components/ui/accordion';

const props = defineProps<{
  bookings: BookingDTO[];
  username: string;
  userId: Uuid;
}>();
</script>

<template>
  <p v-if="props.bookings.length === 0">
    You don't have any bookings yet. Choose your favourite restaurant and book your meal to make it
    appear here.
  </p>

  <Accordion type="single" class="w-full border p-4" collapsible>
    <BookingItem
      v-for="booking in bookings"
      :key="booking.bookingId"
      :booking="booking"
      :username="props.username"
      :userId="props.userId"
    />
  </Accordion>
</template>
