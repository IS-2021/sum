<script setup lang="ts">
import { AspectRatio } from '@/components/ui/aspect-ratio';

defineProps<{
  src: string;
}>();
</script>

<template>
  <AspectRatio aria-hidden="true" :ratio="16 / 9" class="overflow-clip rounded-md">
    <img :src="src" alt="" class="h-full w-full object-cover" />
  </AspectRatio>
</template>
