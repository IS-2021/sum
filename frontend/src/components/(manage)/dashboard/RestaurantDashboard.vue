<script setup lang="ts">
import { useHead } from '@unhead/vue';
import StatsCard from '@/components/(manage)/dashboard/StatsCard.vue';
import { CookingPotIcon, ThumbsUpIcon } from 'lucide-vue-next';
import BookingCard from '@/components/(manage)/dashboard/BookingCard.vue';
import { addMinutes, subDays } from 'date-fns';
import ReportsPage from '../reports/ReportsPage.vue';
import { useUser } from '@/composables/useUser';

useHead({
  title: 'Restaurant Dashboard',
});

const { user } = useUser();
=======
const props = defineProps<{
  restaurant: RestaurantDTO;
}>();

const { data, refetch } = useGetBookings(
  { restaurantId: props.restaurant.id },
  {
    query: {
      refetchInterval: 1000,
    },
  },
);
const bookings = computed(() => unref(data)?.data);
const activeBookings = computed<BookingDTO[]>(() => {
  if (!bookings.value || bookings.value.length === 0) {
    return [];
  }

  return bookings.value.filter((booking) => booking.status === 'Active');
});

async function updateBookingStatus(booking: BookingDTO) {
  const res = await putBookingsId(booking.bookingId, {
    ...booking,
    status: 'PickedUp',
  });

  if (res.status === 200) {
    await refetch();
  }
}
</script>

<template>
  <RestaurantInfo v-if="bookings" :restaurant="restaurant" :bookings="bookings" />

  <div class="flex flex-col gap-10 xl:flex-row">
    <div
      class="space-y-5 p-4 bg-neutral-100 border border-neutral-200 max-w-screen-md w-full flex-grow"
    >
      <div v-if="bookings">
        <h2 class="font-semibold mb-4">Current bookings ({{ activeBookings.length }})</h2>
        <div v-if="activeBookings.length === 0">
          <p>No active bookings</p>
        </div>

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
      </div>
    </div>

    <ReportsPage v-if="user" :restaurantId="user.id" />
  </div>
</template>
