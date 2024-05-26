<script setup lang="ts">
import type { BookingDTO, UserDTO } from '@/lib/api-model';
import { ref } from 'vue';

const props = defineProps<{
  user: UserDTO;
  bookings: BookingDTO[];
}>();

const completedBookings = props.bookings.filter((booking) => !booking.pickedUpTimestamp);
const completedBookingsCount = ref(completedBookings.length);
</script>

<template>
  <div
    class="flex rounded-md mb-8 bg-secondary px-8 py-4 justify-center sm:justify-between flex-wrap"
  >
    <div class="rounded-md flex flex-col justify-center items-start">
      <p v-if="user" class="text-2xl font-bold text-center">Welcome {{ props.user.firstName }}!</p>
      <p class="text-lg text-center">You saved {{ completedBookingsCount }} meals so far!</p>
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
