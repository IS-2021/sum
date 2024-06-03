<script setup lang="ts">
import { AspectRatio } from '@/components/ui/aspect-ratio';

import { getImageUrl } from '@/lib/assets';

const props = defineProps<{
  imageUrl: string | undefined;
  restaurantName: string;
}>();

const imgSrc = getImageUrl(props.imageUrl);
const imageAltText = `Image of ${props.restaurantName}`;
</script>

<template>
  <AspectRatio :ratio="16 / 9" class="overflow-clip rounded-md">
    <img
      :src="imgSrc"
      :alt="imageAltText"
      class="object-cover w-full h-full group-hover:scale-105 transition-transform"
    />
  </AspectRatio>
</template>
