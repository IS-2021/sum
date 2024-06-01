<script setup lang="ts">
import { BanIcon, CircleCheckBigIcon, CircleIcon } from 'lucide-vue-next';
import type { RestaurantDTOStatus } from '@/lib/api-model';

defineProps<{
  status: RestaurantDTOStatus;
}>();
</script>

<template>
  <p v-if="status === 'Active'" class="text-green-500">
    <CircleCheckBigIcon class="inline-block mr-2 h-4 w-4" />
    Restaurant active
  </p>
  <p v-else-if="status === 'Inactive'" class="text-yellow-500">
    <CircleIcon class="inline-block mr-2 h-4 w-4" />
    Restaurant inactive
  </p>
  <p v-else-if="status === 'Banned'" class="text-red-500">
    <BanIcon class="inline-block mr-2 h-4 w-4" />
    Restaurant banned
  </p>
</template>

<style scoped></style>
