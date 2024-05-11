<script setup lang="ts">
import { cn } from '@/lib/utils';
import { openingHoursToString } from '@/components/restaurants/openingHours';

const props = defineProps<{
  day: string;
  openingHours: string;
  closingHours: string;
  isCurrentDay: boolean;
}>();

const hasOpeningHours = props.openingHours && props.closingHours;
const openingHours = hasOpeningHours ? [props.openingHours, props.closingHours] : [];
</script>

<template>
  <p
    :class="
      cn(
        'flex justify-between',
        isCurrentDay && 'text-primary underline underline-offset-4 font-semibold',
      )
    "
  >
    <span class="capitalize">{{ day }}</span>
    <span class="tabular-nums">{{ openingHoursToString(openingHours) }}</span>
  </p>
</template>
