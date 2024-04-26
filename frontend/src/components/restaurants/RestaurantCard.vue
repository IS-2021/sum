<script setup lang="ts">
import { ThumbsUpIcon, ClockIcon } from 'lucide-vue-next';
import { AspectRatio } from '@/components/ui/aspect-ratio';
import fallbackImgUrl from '@/assets/images/restaurant-image-1.jpg';

interface RestaurantCardProps {
  id: string;
  imageSrc?: string;
  name: string;
  rating: Number;
  openingHours: string;
  closingHours: string;
}

const props = defineProps<RestaurantCardProps>();

const restaurantLink = `/restaurant/${props.id}`;

const imageAltText = `${props.name} restaurant`;
const imgSrc = props.imageSrc ?? fallbackImgUrl;
</script>

<template>
  <RouterLink :to="restaurantLink">
    <article class="bg-neutral-900 text-neutral-50 rounded text-lg space-y-3 p-4">
      <AspectRatio :ratio="16 / 9">
        <img :src="imgSrc" :alt="imageAltText" class="rounded-md object-cover w-full h-full" />
      </AspectRatio>

      <header class="flex justify-between items-center">
        <p class="font-bold">{{ name }}</p>

        <p class="flex gap-2 items-center">
          <span class="leading-5">{{ rating }}%</span>
          <ThumbsUpIcon class="w-5 h-5" />
        </p>
      </header>

      <footer class="text-neutral-500">
        <p class="flex items-center gap-2 justify-end">
          <span class="leading-5">{{ openingHours }} - {{ closingHours }}</span>
          <ClockIcon class="w-5 h-5" />
        </p>
      </footer>
    </article>
  </RouterLink>
</template>
