<script setup lang="ts">
import { ThumbsUpIcon } from 'lucide-vue-next';
import { AspectRatio } from '@/components/ui/aspect-ratio';
import RestaurantOpeningHours from '@/components/restaurants/RestaurantOpeningHours.vue';
import type { HoursDTO } from '@/lib/api-model';
import { getImageUrl } from '@/lib/assets';

interface RestaurantCardProps {
  id: string;
  imageSrc?: string;
  name: string;
  rating: Number;
  hours: HoursDTO;
}

const props = defineProps<RestaurantCardProps>();

const restaurantLink = `/restaurant/${props.id}`;

const imageAltText = `${props.name} restaurant`;
const imgSrc = getImageUrl(props.imageSrc);
</script>

<template>
  <RouterLink :to="restaurantLink">
    <article class="bg-neutral-900 text-neutral-50 rounded text-lg space-y-3 p-4 h-full">
      <AspectRatio :ratio="16 / 9">
        <img :src="imgSrc" :alt="imageAltText" class="rounded-md object-cover w-full h-full" />
      </AspectRatio>

      <header class="flex justify-between items-center">
        <p class="font-bold">{{ name }}</p>
      </header>

      <footer class="flex items-center justify-between text-neutral-500">
        <RestaurantOpeningHours :hours="props.hours" />

        <p class="flex gap-2 items-center">
          <span class="leading-5">{{ rating }}%</span>
          <ThumbsUpIcon class="w-5 h-5" />
        </p>
      </footer>
    </article>
  </RouterLink>
</template>
