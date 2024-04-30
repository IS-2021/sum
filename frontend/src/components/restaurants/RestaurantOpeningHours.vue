<script setup lang="ts">
import type { HoursDTO } from '@/lib/api-model';
import { HoverCard, HoverCardContent, HoverCardTrigger } from '@/components/ui/hover-card';
import { ClockIcon } from 'lucide-vue-next';
import { ref } from 'vue';
import OpeningHoursRow from './OpeningHoursRow.vue';

const props = defineProps<{ hours: HoursDTO }>();

const currentDayHours = Object.entries(props.hours).find(
  ([day]) => day === new Date().toLocaleDateString('en-US', { weekday: 'long' }).toLowerCase(),
);

let todayOpeningHours = ref('');
if (currentDayHours && currentDayHours[1]) {
  const [openingHours, closingHours] = currentDayHours[1];
  todayOpeningHours.value = `${openingHours} - ${closingHours}`;
} else {
  todayOpeningHours.value = 'Closed';
}

const isCurrentDay = (day: keyof HoursDTO) => {
  return day === new Date().toLocaleDateString('en-US', { weekday: 'long' }).toLowerCase();
};
</script>

<template>
  <HoverCard>
    <HoverCardTrigger>
      <p class="flex items-center gap-2 justify-end">
        <span class="leading-5">{{ todayOpeningHours }}</span>
        <ClockIcon class="w-5 h-5" />
      </p>
    </HoverCardTrigger>
    <HoverCardContent>
      <ul>
        <li v-for="[day, [openingHours, closingHours]] in Object.entries(props.hours)">
          <OpeningHoursRow
            :day="day"
            :opening-hours="openingHours"
            :closing-hours="closingHours"
            :is-current-day="isCurrentDay(day)"
          />
        </li>
      </ul>
    </HoverCardContent>
  </HoverCard>
</template>
