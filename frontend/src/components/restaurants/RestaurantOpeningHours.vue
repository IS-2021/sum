<script setup lang="ts">
import type { HoursDTO } from '@/lib/api-model';
import { ClockIcon } from 'lucide-vue-next';
import OpeningHoursRow from './OpeningHoursRow.vue';
import { openingHoursToString } from '@/components/restaurants/openingHours';
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from '@/components/ui/tooltip';

const props = defineProps<{ hours: HoursDTO }>();

const isCurrentDay = (day: string) => {
  return day === new Date().toLocaleDateString('en-US', { weekday: 'long' }).toLowerCase();
};

const foundCurrentDayHours = Object.entries(props.hours).find(([day]) => isCurrentDay(day));
const currentDayHours = foundCurrentDayHours ? foundCurrentDayHours[1] : [];
</script>

<template>
  <TooltipProvider>
    <Tooltip>
      <TooltipTrigger>
        <p class="flex items-center gap-2 justify-end">
          <span class="leading-5">{{ openingHoursToString(currentDayHours) }}</span>
          <ClockIcon class="w-5 h-5" />
        </p>
      </TooltipTrigger>
      <TooltipContent class="w-64 text-base" side="bottom">
        <p class="mb-2 text-md font-bold">Opening Hours</p>

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
      </TooltipContent>
    </Tooltip>
  </TooltipProvider>
</template>
