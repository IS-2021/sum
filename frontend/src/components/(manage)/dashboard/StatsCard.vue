<script setup lang="ts">
interface StatsCardProps {
  value: string;
  measure: string;
}

const { value, measure } = defineProps<StatsCardProps>();
</script>

<template>
  <div class="flex gap-8 items-center bg-white px-10 py-5 rounded min-w-72 w-fit justify-center">
    <slot />
    <div>
      <p class="text-3xl font-bold mb-1">{{ value }}</p>
      <p class="text-neutral-500">{{ measure }}</p>
    </div>
  </div>
</template>
