<script setup lang="ts">
import { AccordionContent, AccordionTrigger } from '@/components/ui/accordion';

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

const { data } = useGetRestaurantsId(props.meal.restaurantId);
const restaurant = computed(() => unref(data)?.data);

const isBookingActive = computed(() => {
  console.log(props.booking);
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
      <p>{{ props.booking.orderedTimestamp }}</p>
      <p>{{ props.username }}</p>
    </div>
  </AccordionTrigger>
  <AccordionContent v-if="isBookingActive" class="mt-4"> Status: Active</AccordionContent>
  <AccordionContent v-else class="mt-4"> Status: Disabled</AccordionContent>
  <AccordionContent> Meal: {{ props.meal.name }}</AccordionContent>
  <AccordionContent v-if="restaurant"> Restaurant: {{ restaurant.name }} </AccordionContent>
  <AccordionContent v-if="restaurant">
    Address: {{ restaurant.address.street }}
    {{ restaurant.address.number }}
    {{ restaurant.address.city }}
    {{ restaurant.address.postalCode }}
  </AccordionContent>
  <AccordionContent> Order time: {{ props.booking.orderedTimestamp }}</AccordionContent>
  <AccordionContent v-if="props.booking.pickedUpTimestamp">
    Pick-up time: {{ props.booking.pickedUpTimestamp }}
  </AccordionContent>
  <AccordionContent v-if="restaurant"> Contact: {{ restaurant.phoneNumber }}</AccordionContent>

  <AccordionContent class="mt-4">
    <ReportComponent v-if="restaurant" :restaurantId="restaurant.id" :userId="props.userId" />
  </AccordionContent>
</template>
