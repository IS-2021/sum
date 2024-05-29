<script setup lang="ts">
import { AspectRatio } from '@/components/ui/aspect-ratio';
import type { RestaurantDTO } from '@/lib/api-model';

import { getImageUrl } from '@/lib/assets';

const props = defineProps<{
  restaurant: RestaurantDTO;
}>();

const imgSrc = getImageUrl(props.restaurant.imageUrl);
const imageAltText = `Image of ${props.restaurant.name}`;
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
