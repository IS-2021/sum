<script setup lang="ts">
import { useHead } from '@unhead/vue';
import { subDays } from 'date-fns';
import ReportCard from '@/components/(manage)/dashboard/ReportCard.vue';
import type { BookingDTO, RestaurantDTO } from '@/lib/api-model';
import { computed, unref } from 'vue';
import { putBookingsId, useGetBookings } from '@/lib/api/bookings/bookings';
import BookingCard from '@/components/(manage)/dashboard/BookingCard.vue';
import RestaurantInfo from '@/components/(manage)/dashboard/RestaurantInfo.vue';

useHead({
  title: 'Restaurant Dashboard',
});

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

    <div class="space-y-5 p-4 bg-neutral-100 border border-neutral-200 max-w-screen-md flex-shrink">
      <h2 class="font-semibold">Ratings (17)</h2>
      <ul class="space-y-2">
        <li>
          <ReportCard
            cause="The package contained much less than expected. Meal tasted as if it was microwaved. An eggless omelette?
Wakanda shit is this? The package contained much less than expected. Meal tasted as if it was microwaved. An eggless omelette?
Wakanda shit is this? The package contained much less than expected. Meal tasted as if it was microwaved. An eggless omelette?
Wakanda shit is this? The package contained much less than expected. Meal tasted as if it was microwaved. An eggless omelette?
Wakanda shit is this? The package contained much less than expected. Meal tasted as if it was microwaved. An eggless omelette?
Wakanda shit is this? The package contained much less than expected. Meal tasted as if it was microwaved. An eggless omelette?
Wakanda shit is this? The package contained much less than expected. Meal tasted as if it was microwaved. An eggless omelette?
Wakanda shit is this?"
            :reportedAt="subDays(Date.now(), 3)"
          />
        </li>
        <li>
          <ReportCard
            cause="The package contained much less than expected."
            :reportedAt="subDays(Date.now(), 3)"
          />
        </li>
      </ul>
    </div>
  </div>
</template>
