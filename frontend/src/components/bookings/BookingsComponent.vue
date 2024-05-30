<script setup lang="ts">
import { computed, unref } from 'vue';
import { useGetBookings } from '@/lib/api/bookings/bookings';
import type { Uuid } from '@/lib/api-model';

import BookingsList from '@/components/bookings/BookingsList.vue';
import ToggleToActive from '@/components/bookings/ToggleToActive.vue';

const props = defineProps<{
  userId: Uuid;
  username: string;
}>();

const { data } = useGetBookings({ userId: props.userId });
const bookings = computed(() => unref(data)?.data);
</script>

<template>
  <div class="w-full sm:max-w-screen-sm md:max-w-screen-md lg:max-w-screen-xl mx-auto px-4">
    <div class="w-full sm:flex justify-between mb-8">
      <h1 class="font-bold text-2xl tracking-tight mb-4 sm:mb-0">Bookings</h1>
      <ToggleToActive :userId="props.userId" />
    </div>
    <BookingsList
      v-if="bookings"
      :bookings="bookings"
      :username="props.username"
      :userId="props.userId"
    />
  </div>
</template>
