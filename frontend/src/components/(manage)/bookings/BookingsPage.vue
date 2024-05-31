<script setup lang="ts">
import type { BookingDTO, Uuid } from '@/lib/api-model';
import { putBookingsId, useGetBookings } from '@/lib/api/bookings/bookings';
import { computed, unref } from 'vue';
import BookingCard from '../dashboard/BookingCard.vue';

const props = defineProps<{
  restaurantId: Uuid;
}>();

const { data, refetch, isLoading } = useGetBookings(
  { restaurantId: props.restaurantId },
  {
    query: {
      refetchInterval: 5000,
    },
  },
);
const bookings = computed(() => unref(data)?.data);

async function updateBookingStatus(booking: BookingDTO) {
  const res = await putBookingsId(booking.bookingId, {
    ...booking,
    status: 'PickedUp',
  });

  if (res.status === 200) {
    await refetch();
    location.reload();
  }
}
</script>

<template>
  <h1 class="text-2xl font-semibold tracking-tight mb-10">All Bookings</h1>
  <div v-if="isLoading">Loading...</div>
  <div
    class="space-y-3 p-4 bg-neutral-100 border border-neutral-200 max-w-screen-md w-full flex-grow"
    v-if="bookings && bookings.length !== 0"
  >
    <BookingCard
      v-for="booking in bookings"
      :key="booking.bookingId"
      :booking="booking"
      @on-booking-accept="updateBookingStatus"
    />
  </div>
  <div
    class="space-y-3 p-4 bg-neutral-100 border border-neutral-200 max-w-screen-md w-full flex-grow"
    v-else-if="!bookings || (bookings && bookings.length === 0)"
  >
    <p>No bookings found</p>
  </div>
</template>
