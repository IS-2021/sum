<script setup lang="ts">
interface StatsCardProps {
  value: string;
  measure: string;
}

const { value, measure } = defineProps<StatsCardProps>();
</script>

<template>
  <div
    class="flex gap-4 sm:gap-8 items-center bg-white px-10 sm:py-5 py-3 rounded min-w-72 w-full sm:w-fit sm:justify-center"
  >
    <slot />
    <div class="flex items-center gap-3 sm:block">
      <p class="sm:text-3xl font-bold sm:mb-1">{{ value }}</p>
      <p class="text-neutral-500">{{ measure }}</p>
    </div>
  </div>
</template>
