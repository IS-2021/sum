<script setup lang="ts">
import { useHead } from '@unhead/vue';
import StatsCard from '@/components/(manage)/dashboard/StatsCard.vue';
import { CookingPotIcon, ThumbsUpIcon } from 'lucide-vue-next';
import BookingCard from '@/components/(manage)/dashboard/BookingCard.vue';
import { addMinutes, subDays } from 'date-fns';

useHead({
  title: 'Restaurant Dashboard',
});
</script>

<template>
  <h1 class="text-2xl font-semibold tracking-tight mb-10">
    Some local restaurant name that is kinda long
  </h1>

  <div class="flex gap-6 mb-8">
    <StatsCard measure="Positive ratings" value="97%">
      <ThumbsUpIcon class="w-9 h-9 text-primary" />
    </StatsCard>
    <StatsCard measure="Meals saved" value="34">
      <CookingPotIcon class="w-9 h-9 text-primary" />
    </StatsCard>
  </div>

  <div class="space-y-5 p-4 bg-neutral-100 border border-neutral-200">
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
</template>
