<script setup lang="ts">
import { Accordion, AccordionItem } from '@/components/ui/accordion';

import { computed, ref, unref } from 'vue';
import type { BookingDTO } from '@/lib/api-model';

import BookingBody from '@/components/bookings/BookingBody.vue';
import { useGetMealsId } from '@/lib/api/meals/meals';

const props = defineProps<{
  booking: BookingDTO;
  username: string;
}>();

const defaultValue = 'booking';
let open = ref(false);

const { data } = useGetMealsId(props.booking.mealId);
const meal = computed(() => unref(data)?.data);

function isOpen() {
  open.value = !open.value;
}
</script>

<template>
  <Accordion
    v-if="props.booking"
    type="single"
    class="w-full border p-4"
    collapsible
    :default-value="defaultValue"
  >
    <AccordionItem :value="defaultValue" :open="open" @click="isOpen()">
      <BookingBody v-if="meal" :booking="props.booking" :username="props.username" :meal="meal" />
    </AccordionItem>
  </Accordion>
</template>
