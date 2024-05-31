<script setup lang="ts">
import type { BookingDTO } from '@/lib/api-model';
import { putBookingsId } from '@/lib/api/bookings/bookings';
import BookingCard from './BookingCard.vue';
import { ref } from 'vue';

const props = defineProps<{
  bookings: BookingDTO[];
  activeBookings: BookingDTO[] | null;
}>();

const emits = defineEmits<{
  (e: 'updateCurrentBookings', activeBookings: BookingDTO[]): void;
}>();

const activeBookings = ref(props.bookings.filter((booking) => booking.status === 'Active'));

async function updateBookingStatus(booking: BookingDTO) {
  const res = await putBookingsId(booking.bookingId, {
    ...booking,
    status: 'PickedUp',
  });

  if (res.status === 200) {
    location.reload();
  }
}

emits('updateCurrentBookings', activeBookings);
</script>

<template>
  <ul v-if="bookings.length !== 0" class="space-y-2">
    <li class="space-y-3">
      <BookingCard
        v-for="booking in activeBookings"
        :key="booking.bookingId"
        :booking="booking"
        @on-booking-accept="updateBookingStatus"
      />
    </li>
  </ul>
</template>
