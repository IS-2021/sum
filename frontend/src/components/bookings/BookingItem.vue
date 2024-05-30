<script setup lang="ts">
import { Accordion, AccordionItem } from '@/components/ui/accordion';

import { computed, ref, unref } from 'vue';
import type { BookingDTO, Uuid } from '@/lib/api-model';

import BookingBody from '@/components/bookings/BookingBody.vue';
import { useGetMealsId } from '@/lib/api/meals/meals';

const props = defineProps<{
  booking: BookingDTO;
  username: string;
  userId: Uuid;
}>();

const open = ref(false);

const { data } = useGetMealsId(props.booking.mealId);
const meal = computed(() => unref(data)?.data);

function changeIsOpen(o: boolean) {
  open.value = o;
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
      <BookingBody
        v-if="meal"
        :booking="props.booking"
        :username="props.username"
        :userId="props.userId"
        :meal="meal"
        :open="open"
        @change-is-open="changeIsOpen"
      />
    </AccordionItem>
  </Accordion>
</template>
