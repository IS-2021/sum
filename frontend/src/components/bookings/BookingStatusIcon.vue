<script setup lang="ts">
import { CircleCheckBigIcon, CircleIcon, CircleSlash2Icon, CircleXIcon } from 'lucide-vue-next';
import type { BookingStatus } from '@/lib/api-model';

defineProps<{
  status: BookingStatus;
}>();
</script>

<template>
  <CircleCheckBigIcon v-if="status === 'PickedUp'" />
  <CircleXIcon v-else-if="status === 'Cancelled'" />
  <CircleIcon v-else-if="status === 'Active'" />
  <CircleSlash2Icon v-else-if="status === 'OutOfDate'" />
</template>
