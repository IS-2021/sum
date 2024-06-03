<script setup lang="ts">
import type { BookingDTO } from '@/lib/api-model';

import LatestBookingBody from '@/components/homepage/LatestBookingBody.vue';

import Button from '@/components/ui/button/Button.vue';

const props = defineProps<{
  bookings: BookingDTO[];
}>();
</script>

<template>
  <div v-if="bookings" class="mb-8">
    <div class="mb-4 w-full justify-between sm:flex">
      <h1 class="mb-4 text-2xl font-bold sm:mb-0">Your Last Bookings</h1>
      <RouterLink to="/bookings">
        <Button>View all bookings</Button>
      </RouterLink>
    </div>
    <div class="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
      <div v-for="booking in bookings.slice(0, 3)" v-bind:key="booking.id">
        <LatestBookingBody :booking="booking" />
      </div>
    </div>
  </div>
</template>
