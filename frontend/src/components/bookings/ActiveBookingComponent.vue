<script setup lang="ts">
import Button from '@/components/ui/button/Button.vue';
import { computed, unref } from 'vue';
import type { Uuid } from '@/lib/api-model';

import { useGetBookingsActive } from '@/lib/api/bookings/bookings';

import ActiveBookingBody from '@/components/bookings/ActiveBookingBody.vue';
import CancelBooking from '@/components/bookings/CancelBooking.vue';

const props = defineProps<{
  userId: Uuid;
}>();

const { data } = useGetBookingsActive({ userId: props.userId });
const activeBooking = computed(() => unref(data)?.data);
</script>

<template>
  <div class="mx-auto w-full px-4 sm:max-w-screen-sm md:max-w-screen-md lg:max-w-screen-xl">
    <div class="mb-8 flex items-center justify-between">
      <h1 class="text-2xl font-bold tracking-tight">Active Booking</h1>
      <CancelBooking v-if="activeBooking" :booking="activeBooking" />
    </div>
    <div class="w-full border border-neutral-200 bg-neutral-100 p-4">
      <h2 class="mb-4 font-semibold">Booking details</h2>
      <p v-if="!activeBooking?.bookingId">
        You don't have any active bookings. Press the button below to view your recent bookings.
      </p>

      <ActiveBookingBody v-if="activeBooking?.bookingId" :activeBooking="activeBooking" />
    </div>
    <Button class="mt-8" as-child>
      <RouterLink to="/bookings">View all bookings</RouterLink>
    </Button>
  </div>
</template>
