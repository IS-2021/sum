<script setup lang="ts">
import { useHead } from '@unhead/vue';
import StatsCard from '@/components/(manage)/dashboard/StatsCard.vue';
import { CookingPotIcon, ThumbsUpIcon } from 'lucide-vue-next';
import { subDays } from 'date-fns';
import ReportCard from '@/components/(manage)/dashboard/ReportCard.vue';
import type { BookingDTO, Uuid } from '@/lib/api-model';
import { computed, ref, unref, type Ref } from 'vue';
import { useGetBookings } from '@/lib/api/bookings/bookings';
import CurrentBookings from '@/components/(manage)/dashboard/CurrentBookings.vue';

useHead({
  title: 'Restaurant Dashboard',
});

const props = defineProps<{
  restaurantId: Uuid;
}>();

const { data } = useGetBookings(
  { restaurantId: props.restaurantId },
  {
    query: {
      refetchInterval: 5000,
    },
  },
);
const bookings = computed(() => unref(data)?.data);

const activeBookings: Ref<BookingDTO[] | null> = ref(null);

function updateActiveBookings(active: BookingDTO[]) {
  activeBookings.value = active;
}
</script>

<template>
  <h1 class="text-2xl font-semibold tracking-tight mb-10">
    Some local restaurant name that is kinda long
  </h1>

  <div class="flex sm:gap-6 mb-8 flex-col sm:flex-row">
    <StatsCard measure="Positive ratings" value="97%">
      <ThumbsUpIcon class="sm:w-9 sm:h-9 text-primary" />
    </StatsCard>
    <StatsCard measure="Meals saved" value="34">
      <CookingPotIcon class="sm:w-9 sm:h-9 text-primary" />
    </StatsCard>
  </div>

  <div class="flex flex-col gap-10 xl:flex-row">
    <div
      class="space-y-5 p-4 bg-neutral-100 border border-neutral-200 max-w-screen-md w-full flex-grow"
    >
      <div v-if="bookings">
        <h2 v-if="activeBookings && activeBookings.length !== 0" class="font-semibold mb-4">
          Current bookings ({{ activeBookings.length }})
        </h2>
        <div v-else-if="!activeBookings || (activeBookings && activeBookings.length === 0)">
          <h2 class="font-semibold mb-4">Active bookings (0)</h2>
          <p>No active bookings found</p>
        </div>
        <CurrentBookings
          :bookings="bookings"
          :activeBookings="activeBookings"
          @updateCurrentBookings="updateActiveBookings"
        />
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
