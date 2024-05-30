<script setup lang="ts">
import type { BookingDTO } from '@/lib/api-model';
import { ref } from 'vue';

const props = defineProps<{
  bookings: BookingDTO[];
}>();

const completedBookingsCount = props.bookings.filter(
  (booking) => !booking.pickedUpTimestamp,
).length;
</script>

<template>
  <p class="text-lg text-center">You saved {{ completedBookingsCount }} meals so far!</p>
</template>
