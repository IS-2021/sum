<script setup lang="ts">
import { AccordionContent, AccordionTrigger } from '@/components/ui/accordion';

import { formatAddress } from '@/lib/googleMaps';

import { formatDate } from '@/lib/formatters';

import type { BookingDTO, MealDTO, Uuid } from '@/lib/api-model';
import { computed, ref, unref } from 'vue';
import { useGetRestaurantsId } from '@/lib/api/restaurants/restaurants';

import ReportComponent from '@/components/bookings/ReportComponent.vue';

const props = defineProps<{
  booking: BookingDTO;
  username: string;
  userId: Uuid;
  meal: MealDTO;
  open: boolean;
}>();

let open = ref(props.open);

const emit = defineEmits<{
  (e: 'changeIsOpen', open: boolean): void;
}>();

const buttonMessage = ref('Report restaurant');

const { data } = useGetRestaurantsId(props.meal.restaurantId);
const restaurant = computed(() => unref(data)?.data);

const isBookingActive = computed(() => {
  if (!props.booking.pickedUpTimestamp) {
    return true;
  } else {
    return false;
  }
});

function isOpen() {
  open.value = !open.value;
  emit('changeIsOpen', open.value);
}
</script>

<template>
  <AccordionTrigger @click="isOpen()">
    <div class="flex flex-col items-start">
      <p class="font-bold text-lg">{{ props.meal.name }}</p>
      <p>{{ formatDate(props.booking.orderedTimestamp) }}</p>
      <p>{{ props.username }}</p>
    </div>
  </AccordionTrigger>
  <AccordionContent>
    <div>
      <p v-if="isBookingActive" class="mt-4">Status: Active</p>
      <p v-else class="mt-4">Status: Disabled</p>

      <p>Meal: {{ props.meal.name }}</p>
      <p v-if="restaurant">Restaurant: {{ restaurant.name }}</p>
      <p v-if="restaurant">Address: {{ formatAddress(restaurant.address) }}</p>
      <p>
        Order time:
        {{ formatDate(props.booking.orderedTimestamp) }}
      </p>
      <p v-if="props.booking.pickedUpTimestamp">
        Pick-up time:
        {{ formatDate(props.booking.pickedUpTimestamp) }}
      </p>
      <p v-if="restaurant">Contact: {{ restaurant.phoneNumber }}</p>
      <div class="mt-4">
        <ReportComponent
          v-if="restaurant"
          :restaurantId="restaurant.id"
          :userId="props.userId"
          :buttonMessage="buttonMessage"
        />
      </div>
    </div>
  </AccordionContent>
</template>
