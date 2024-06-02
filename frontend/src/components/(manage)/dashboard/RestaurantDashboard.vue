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
      <h2 class="font-semibold">Current bookings (2)</h2>
      <ul class="space-y-2">
        <li>
          <BookingCard mealName="Hawaian" :pickupAt="addMinutes(Date.now(), 58)" />
        </li>
        <li>
          <BookingCard mealName="Capriciossa" :pickupAt="addMinutes(Date.now(), 43)" />
        </li>
      </ul>

      <h2 class="font-semibold">Past bookings</h2>
      <ul class="space-y-2">
        <li>
          <BookingCard mealName="Capriciossa" :pickupAt="subDays(Date.now(), 2)" />
        </li>
        <li>
          <BookingCard mealName="Capriciossa" :pickupAt="subDays(Date.now(), 3)" />
        </li>
        <li>
          <BookingCard
            mealName="Lorem ipsum dolor sit amet consectetur. Senectus a egestas arcu quis urna."
            :pickupAt="subDays(Date.now(), 3)"
          />
        </li>
      </ul>
    </div>

    <ReportsPage v-if="user" :restaurantId="user.id" />
  </div>
</template>
