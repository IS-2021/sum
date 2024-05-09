<script setup lang="ts">
import { ThumbsUpIcon } from 'lucide-vue-next';
import { AspectRatio } from '@/components/ui/aspect-ratio';
import RestaurantOpeningHours from '@/components/restaurants/RestaurantOpeningHours.vue';
import type { HoursDTO } from '@/lib/api-model';
import { getImageUrl } from '@/lib/assets';
import UserRating from '@/components/user-rating/UserRating.vue';

interface RestaurantCardProps {
  id: string;
  imageSrc?: string;
  name: string;
  hours: HoursDTO;
  likes: number;
  dislikes: number;
}

const props = defineProps<RestaurantCardProps>();

const restaurantLink = `/restaurant/${props.id}`;

const imageAltText = `${props.name} restaurant`;
const imgSrc = getImageUrl(props.imageSrc);
</script>

<template>
  <RouterLink :to="restaurantLink">
    <article
      class="group bg-neutral-200 text-neutral-900 rounded text-lg space-y-3 p-4 h-full hover:shadow transition-shadow"
    >
      <AspectRatio :ratio="16 / 9" class="overflow-clip rounded-md">
        <img
          :src="imgSrc"
          :alt="imageAltText"
          class="object-cover w-full h-full group-hover:scale-105 transition-transform"
        />
      </AspectRatio>

      <header class="flex justify-between items-center">
        <p class="font-bold">{{ name }}</p>
      </header>

      <footer class="flex items-center justify-between text-neutral-500 text-base">
        <RestaurantOpeningHours :hours="props.hours" />

        <p class="flex gap-2 items-center">
          <UserRating :likes="likes" :dislikes="dislikes" class="leading-5" />
          <ThumbsUpIcon class="w-5 h-5" />
        </p>
      </footer>
    </article>
  </RouterLink>
</template>
