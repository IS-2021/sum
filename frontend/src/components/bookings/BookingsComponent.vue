<script setup lang="ts">
import { computed, unref } from 'vue';
import { useGetBookings } from '@/lib/api/bookings/bookings';
import type { Uuid } from '@/lib/api-model';
import ToggleToActive from '@/components/bookings/ToggleToActive.vue';
import BookingItem from '@/components/bookings/BookingItem.vue';
import { Accordion } from '@/components/ui/accordion';

const props = defineProps<{
  userId: Uuid;
  username: string;
}>();

const { data } = useGetBookings({ userId: props.userId });
const bookings = computed(() => unref(data)?.data ?? []);
</script>

<template>
  <div class="mx-auto w-full px-4 sm:max-w-screen-sm md:max-w-screen-md lg:max-w-screen-xl">
    <div class="mb-8 w-full justify-between sm:flex">
      <h1 class="mb-4 text-2xl font-bold tracking-tight sm:mb-0">All Bookings</h1>
      <ToggleToActive :userId="props.userId" />
    </div>

    <Accordion type="single" collapsible v-if="bookings.length > 0">
      <BookingItem
        v-for="booking in bookings"
        :key="booking.bookingId"
        :booking="booking"
        :username="props.username"
        :userId="props.userId"
      />
    </Accordion>
    <p v-else>
      You don't have any bookings yet. Choose your favourite restaurant and book your meal to make
      it appear here.
    </p>
  </div>
</template>
