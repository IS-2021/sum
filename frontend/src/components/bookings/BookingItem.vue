<script setup lang="ts">
import {
  Accordion,
  AccordionItem,
  AccordionContent,
  AccordionTrigger,
} from '@/components/ui/accordion';

import { formatAddress } from '@/lib/googleMaps';
import { formatDate } from '@/lib/formatters';

import { ref } from 'vue';
import type { BookingDTO, Uuid } from '@/lib/api-model';

import ReportComponent from '@/components/bookings/ReportComponent.vue';
import CancelBooking from '@/components/bookings/CancelBooking.vue';

const props = defineProps<{
  booking: BookingDTO;
  username: string;
  userId: Uuid;
}>();

const buttonMessage = ref('Report restaurant');

const meal = props.booking.meal;
const restaurant = props.booking.restaurant;
</script>

<template>
  <AccordionItem :value="booking.bookingId" v-if="props.booking && meal">
    <AccordionTrigger>
      <div class="flex flex-col items-start">
        <p class="text-lg font-bold">{{ meal.name }}</p>
        <p>{{ formatDate(props.booking.orderedTimestamp) }}</p>
        <p>{{ props.username }}</p>
        <p v-if="props.booking.status === 'Active'" class="font-semi-bold text-primary">
          {{ props.booking.status }}
        </p>
        <p
          v-else-if="props.booking.status === 'Cancelled' || props.booking.status === 'OutOfDate'"
          class="font-semi-bold text-red-600"
        >
          {{ props.booking.status }}
        </p>
        <p v-else class="font-semi-bold">
          {{ props.booking.status }}
        </p>
      </div>
    </AccordionTrigger>
    <AccordionContent>
      <div>
        <p class="mt-4">Status: {{ booking.status }}</p>

        <p>Meal: {{ meal.name }}</p>
        <p>Restaurant: {{ restaurant.name }}</p>
        <p>Address: {{ formatAddress(restaurant.address) }}</p>
        <p>
          Order time:
          {{ formatDate(props.booking.orderedTimestamp) }}
        </p>
        <p v-if="props.booking.pickedUpTimestamp">
          Pick-up time:
          {{ formatDate(props.booking.pickedUpTimestamp) }}
        </p>
        <p>Contact: {{ restaurant.phoneNumber }}</p>
        <div class="mt-4">
          <ReportComponent
            v-if="restaurant"
            :restaurantId="restaurant.id"
            :userId="props.userId"
            :buttonMessage="buttonMessage"
            :bookingStatus="booking.status"
          />
          <CancelBooking :booking="props.booking" />
        </div>
      </div>
    </AccordionContent>
  </AccordionItem>
</template>
