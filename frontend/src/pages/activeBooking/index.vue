<route lang="yaml">
meta:
  layout: default
</route>

<script setup lang="ts">
import { useHead } from '@unhead/vue';

import { getMeal, getRestaurant } from '@/components/bookings/bookings';

import Button from '@/components/ui/button/Button.vue';
import { useUser } from '@/composables/useUser';
import { useGetBookingsActive } from '@/lib/api/bookings/bookings';
import { computed, unref, type Ref } from 'vue';
import type { BookingDTO } from '@/lib/api-model';

useHead({
  title: 'Active Booking - FoodGood',
});

const { user } = useUser();

let activeBooking: Ref<BookingDTO | undefined>;

if (user.value) {
  const { data } = useGetBookingsActive({ userId: user.value.id });
  activeBooking = computed(() => unref(data)?.data);
}
</script>

<template>
  <div class="w-full sm:max-w-screen-sm md:max-w-screen-md lg:max-w-screen-xl mx-auto px-4">
    <h1 class="font-bold text-2xl mb-8 tracking-tight">Active Booking</h1>
    <div class="space-y-5 p-4 bg-neutral-100 border border-neutral-200 w-full">
      <h2 class="font-semibold">Order details</h2>
      <p v-if="!activeBooking?.id">You don't have any active bookings.</p>
      <ul v-if="activeBooking?.id" class="space-y-2">
        <li>
          <p>Meal: {{ getMeal(activeBooking.mealId)?.name }}</p>
        </li>
        <li>
          <p>Restaurant: {{ getRestaurant(activeBooking.mealId)?.name }}</p>
        </li>
        <li>
          <p>
            Address: {{ getRestaurant(activeBooking.mealId)?.address.street }}
            {{ getRestaurant(activeBooking.mealId)?.address.number }}
            {{ getRestaurant(activeBooking.mealId)?.address.city }}
            {{ getRestaurant(activeBooking.mealId)?.address.postalCode }}
          </p>
        </li>
        <li>
          <p>Order time: {{ activeBooking.orderedTimestamp }}</p>
        </li>
        <li>
          <p>Pick-up deadline: {{ activeBooking.deadlinePickUpTimestamp }}</p>
        </li>
        <li>
          <p class="mt-8">Contact: {{ getRestaurant(activeBooking.mealId)?.phoneNumber }}</p>
        </li>
      </ul>
    </div>
    <RouterLink to="/bookings">
      <Button class="mt-8">View all bookings</Button>
    </RouterLink>
  </div>
</template>
