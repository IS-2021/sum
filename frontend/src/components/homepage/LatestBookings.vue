<script setup lang="ts">
import type { Uuid } from '@/lib/api-model';

import LatestBookingBody from '@/components/homepage/LatestBookingBody.vue';
import { useGetBookings } from '@/lib/api/bookings/bookings';
import { computed, unref } from 'vue';

import Button from '../ui/button/Button.vue';

const props = defineProps<{
  userId: Uuid;
}>();

const { data } = useGetBookings({ userId: props.userId });
const bookings = computed(() => unref(data)?.data);
</script>

<template>
  <div v-if="bookings" class="mb-8">
    <div class="w-full sm:flex justify-between mb-8">
      <h1 class="font-bold text-2xl mb-4 sm:mb-0">Your Last Bookings</h1>
      <RouterLink to="/bookings">
        <Button>View all bookings</Button>
      </RouterLink>
    </div>
    <div class="grid md:grid-cols-2 lg:grid-cols-3 gap-4">
      <div v-for="booking in bookings.slice(0, 3)" v-bind:key="booking.id">
        <LatestBookingBody :booking="booking" />
      </div>
    </div>
  </div>
</template>
