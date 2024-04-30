<script setup lang="ts">
import type { HoursDTO } from '@/lib/api-model';

const props = defineProps<{ day: keyof HoursDTO; openingHours: string; closingHours: string }>();

const hasOpeningHours = props.openingHours && props.closingHours;
</script>

<template>
  <p class="flex justify-between">
    <span class="capitalize">{{ day }}</span>
    <span v-if="hasOpeningHours" class="tabular-nums">{{ openingHours }} - {{ closingHours }}</span>
    <span v-else>Closed</span>
  </p>
</template>
