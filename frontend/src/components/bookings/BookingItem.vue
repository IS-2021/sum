<script setup lang="ts">
import { AccordionContent, AccordionItem, AccordionTrigger } from '@/components/ui/accordion';

import { formatAddress } from '@/lib/googleMaps';
import { formatDate } from '@/lib/formatters';

import { ref } from 'vue';
import type { BookingDTO, Uuid } from '@/lib/api-model';

import ReportComponent from '@/components/bookings/ReportComponent.vue';
import CancelBooking from '@/components/bookings/CancelBooking.vue';
import { cn } from '@/lib/utils';
import BookingStatusIcon from '@/components/bookings/BookingStatusIcon.vue';
import RestaurantCard from '@/components/(admin)/restaurants/RestaurantCard.vue';
import { Separator } from '@/components/ui/separator';
import BookingRestaurantCard from '@/components/bookings/BookingRestaurantCard.vue';

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
      <div class="items-star space-y-2 text-left">
        <p class="text-lg font-bold">{{ meal.name }}</p>
        <p>{{ formatDate(props.booking.orderedTimestamp) }}</p>
        <p
          :class="
            cn('font-semi-bold flex items-center gap-2', {
              'text-primary': props.booking.status === 'Active',
              'text-red-600': ['Cancelled', 'OutOfDate'].includes(props.booking.status),
            })
          "
        >
          <BookingStatusIcon :status="booking.status" class="size-4" /> {{ booking.status }}
        </p>
      </div>
    </AccordionTrigger>
    <AccordionContent>
      <div>
        <BookingRestaurantCard :restaurant="restaurant" :show-status="false" />

        <div class="mb-2 mt-4 flex justify-between">
          <div>
            <!-- Like/dislike button -->
            <CancelBooking :booking="props.booking" />
          </div>
        </div>

        <ReportComponent
          v-if="restaurant"
          :restaurantId="restaurant.id"
          :userId="props.userId"
          :buttonMessage="buttonMessage"
          :bookingStatus="booking.status"
        />
      </div>
    </AccordionContent>
  </AccordionItem>
</template>
