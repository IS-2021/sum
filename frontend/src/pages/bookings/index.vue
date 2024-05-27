<route lang="yaml">
meta:
  layout: default
</route>

<script setup lang="ts">
import { useHead } from '@unhead/vue';

import Button from '@/components/ui/button/Button.vue';

import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from '@/components/ui/accordion';
import { computed, ref, unref } from 'vue';
import { useUser } from '@/composables/useUser';
import { useGetBookings, useGetBookingsActive } from '@/lib/api/bookings/bookings';

import { getMeal, getRestaurant } from '@/components/bookings/bookings';

useHead({
  title: 'Bookings - FoodGood',
});

const { user } = useUser();
const { data } = useGetBookings({ userId: user.value?.id });
const bookings = unref(data)?.data;

const defaultValue = 'booking';
let open = ref(false);

function isOpen() {
  open.value = !open.value;
}

function getActiveBooking() {
  if (user.value) {
    const { data } = useGetBookingsActive({ userId: user.value.id });
    const activeBooking = unref(data)?.data;
    if (activeBooking?.id) {
      return true;
    } else {
      return false;
    }
  }
}

const activeBooking = computed(() => getActiveBooking());
</script>

<template>
  <div
    v-if="user"
    class="w-full sm:max-w-screen-sm md:max-w-screen-md lg:max-w-screen-xl mx-auto px-4"
  >
    <div class="w-full flex justify-between">
      <h1 class="font-bold text-2xl mb-8 tracking-tight">Bookings</h1>
      <RouterLink v-if="activeBooking" to="/activeBooking">
        <Button>Go to active booking</Button>
      </RouterLink>
      <Button v-else disabled>Go to active booking</Button>
    </div>
    <p v-if="!bookings || bookings.length === 0">
      You don't have any bookings yet. Choose your favourite restaurant and book your meal to make
      it appear here.
    </p>
    <Accordion
      v-if="bookings && bookings.length !== 0"
      type="single"
      class="w-full border p-4"
      collapsible
      :default-value="defaultValue"
    >
      <AccordionItem
        v-for="booking in bookings"
        v-bind:key="booking.id"
        :value="defaultValue"
        :open="open"
        @click="isOpen()"
      >
        <AccordionTrigger>
          <div class="flex flex-col items-start">
            <p class="font-bold text-lg">{{ getMeal(booking.mealId)?.name }}</p>
            <p>{{ booking.orderedTimestamp }}</p>
            <p>{{ user.username }}</p>
          </div>
        </AccordionTrigger>
        <AccordionContent v-if="activeBooking" class="mt-4"> Status: Active</AccordionContent>
        <AccordionContent v-else class="mt-4"> Status: Disabled</AccordionContent>
        <AccordionContent> Meal: {{ getMeal(booking.mealId)?.name }}</AccordionContent>
        <AccordionContent> Restaurant: {{ getRestaurant(booking.mealId)?.name }} </AccordionContent>
        <AccordionContent>
          Address: {{ getRestaurant(booking.mealId)?.address.street }}
          {{ getRestaurant(booking.mealId)?.address.number }}
          {{ getRestaurant(booking.mealId)?.address.city }}
          {{ getRestaurant(booking.mealId)?.address.postalCode }}
        </AccordionContent>
        <AccordionContent> Order time: {{ booking.orderedTimestamp }}</AccordionContent>
        <AccordionContent v-if="booking.pickedUpTimestamp">
          Pick-up time: {{ booking.pickedUpTimestamp }}
        </AccordionContent>
        <AccordionContent>
          Contact: {{ getRestaurant(booking.mealId)?.phoneNumber }}</AccordionContent
        >
        <AccordionContent class="mt-4">
          <Button>Report restaurant</Button>
        </AccordionContent>
      </AccordionItem>
    </Accordion>
  </div>
</template>
