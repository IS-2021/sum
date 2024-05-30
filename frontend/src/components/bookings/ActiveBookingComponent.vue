<script setup lang="ts">
import Button from '@/components/ui/button/Button.vue';
import { computed, unref } from 'vue';
import type { Uuid } from '@/lib/api-model';

import { useGetBookingsActive } from '@/lib/api/bookings/bookings';

import ActiveBookingBody from '@/components/bookings/ActiveBookingBody.vue';

const props = defineProps<{
  userId: Uuid;
}>();

const { data } = useGetBookingsActive({ userId: props.userId });
const activeBooking = computed(() => unref(data)?.data);
</script>

<template>
  <div class="w-full sm:max-w-screen-sm md:max-w-screen-md lg:max-w-screen-xl mx-auto px-4">
    <h1 class="font-bold text-2xl mb-8 tracking-tight">Active Booking</h1>
    <div class="space-y-5 p-4 bg-neutral-100 border border-neutral-200 w-full">
      <h2 class="font-semibold">Booking details</h2>
      <p v-if="!activeBooking">You don't have any active bookings.</p>
      <ActiveBookingBody v-if="activeBooking?.id" :activeBooking="activeBooking" />
    </div>
    <Button class="mt-8" as-child>
      <RouterLink to="/bookings">View all bookings</RouterLink>
    </Button>
  </div>
</template>
