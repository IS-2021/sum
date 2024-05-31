<script setup lang="ts">
import { putBookingsId } from '@/lib/api/bookings/bookings';
import { toast } from 'vue-sonner';

import Button from '../ui/button/Button.vue';
import type { BookingDTO } from '@/lib/api-model';

const props = defineProps<{
  booking: BookingDTO;
}>();

async function cancelBooking() {
  const res = await putBookingsId(props.booking.bookingId, {
    bookingId: props.booking.bookingId,
    deadlinePickUpTimestamp: props.booking.deadlinePickUpTimestamp,
    meal: props.booking.meal,
    orderedTimestamp: props.booking.orderedTimestamp,
    pickedUpTimestamp: props.booking.pickedUpTimestamp,
    restaurant: props.booking.restaurant,
    status: 'Cancelled',
    userId: props.booking.userId,
  });

  if (res.status === 200) {
    location.reload();
    toast.success('Booking cancelled successfully!');
  }
}
</script>

<template>
  <Button v-if="props.booking.status === 'Active'" variant="ghost" @click="cancelBooking"
    >Cancel Booking</Button
  >
</template>
