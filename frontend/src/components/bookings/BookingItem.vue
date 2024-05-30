<script setup lang="ts">
import {
  Accordion,
  AccordionItem,
  AccordionContent,
  AccordionTrigger,
} from '@/components/ui/accordion';
import Button from '../ui/button/Button.vue';

import { formatAddress } from '@/lib/googleMaps';
import { formatDate } from '@/lib/formatters';

import { ref } from 'vue';
import type { BookingDTO, Uuid } from '@/lib/api-model';

import { toast } from 'vue-sonner';

import ReportComponent from '@/components/bookings/ReportComponent.vue';
import { putBookingsId } from '@/lib/api/bookings/bookings';
import { useRoute, useRouter } from 'vue-router';

const props = defineProps<{
  booking: BookingDTO;
  username: string;
  userId: Uuid;
}>();

const open = ref(false);
const buttonMessage = ref('Report restaurant');

const meal = props.booking.meal;
const restaurant = props.booking.restaurant;

function isOpen() {
  open.value = !open.value;
}

async function cancelBooking() {
  const res = await putBookingsId(props.booking.bookingId, {
    bookingId: props.booking.bookingId,
    deadlinePickUpTimestamp: props.booking.deadlinePickUpTimestamp,
    meal: props.booking.meal,
    orderedTimestamp: props.booking.orderedTimestamp,
    pickedUpTimestamp: props.booking.pickedUpTimestamp,
    restaurant: props.booking.restaurant,
    status: 'Cancelled',
    userId: props.booking.userId,
  });

  if (res.status === 200) {
    location.reload();
    toast.success('Booking cancelled successfully!');
  }
}
</script>

<template>
  <Accordion
    v-if="props.booking && meal"
    type="single"
    class="w-full border p-4"
    collapsible
    :default-value="meal.name"
  >
    <AccordionItem :value="meal.name" :open="open">
      <AccordionTrigger @click="isOpen()">
        <div class="flex flex-col items-start">
          <p class="font-bold text-lg">{{ meal.name }}</p>
          <p>{{ formatDate(props.booking.orderedTimestamp) }}</p>
          <p>{{ props.username }}</p>
          <p v-if="props.booking.status === 'Active'" class="text-primary font-semi-bold">
            {{ props.booking.status }}
          </p>
          <p
            v-else-if="props.booking.status === 'Cancelled' || props.booking.status === 'OutOfDate'"
            class="text-red-600 font-semi-bold"
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
            <Button v-if="props.booking.status === 'Active'" variant="ghost" @click="cancelBooking"
              >Cancel</Button
            >
          </div>
        </div>
      </AccordionContent>
    </AccordionItem>
  </Accordion>
</template>
