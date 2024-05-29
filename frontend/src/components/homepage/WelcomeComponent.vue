<script setup lang="ts">
import type { UserDTO } from '@/lib/api-model';

import CompletedBookingsCount from './CompletedBookingsCount.vue';
import { useGetBookings } from '@/lib/api/bookings/bookings';
import { computed, unref } from 'vue';

const props = defineProps<{
  user: UserDTO;
}>();

const { data } = useGetBookings({ userId: props.user.id });
const bookings = computed(() => unref(data)?.data);
</script>

<template>
  <div
    class="flex rounded-md mb-8 bg-secondary px-8 py-4 justify-center sm:justify-between flex-wrap"
  >
    <div class="rounded-md flex flex-col justify-center items-start">
      <p v-if="user" class="text-2xl font-bold text-center">Welcome {{ props.user.firstName }}!</p>
      <CompletedBookingsCount v-if="bookings" :bookings="bookings" />
    </div>
    <div class="flex gap-1 items-end ml-8 mt-8">
      <h1 class="text-primary">FoodGood team</h1>
      <div class="h-4 w-4 mb-1">
        <RouterLink to="/">
          <img src="@/assets/logo.svg" alt="Logo" class="" />
        </RouterLink>
      </div>
    </div>
  </div>
</template>
