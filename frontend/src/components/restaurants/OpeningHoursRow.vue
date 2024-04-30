<script setup lang="ts">
import type { HoursDTO } from '@/lib/api-model';
import { cn } from '@/lib/utils';

const props = defineProps<{
  day: keyof HoursDTO;
  openingHours: string;
  closingHours: string;
  isCurrentDay: boolean;
}>();

const hasOpeningHours = props.openingHours && props.closingHours;
</script>

<template>
  <p :class="cn('flex justify-between', isCurrentDay && 'text-green-500 font-semibold')">
    <span class="capitalize">{{ day }}</span>
    <span v-if="hasOpeningHours" class="tabular-nums">{{ openingHours }} - {{ closingHours }}</span>
    <span v-else>Closed</span>
  </p>
</template>
