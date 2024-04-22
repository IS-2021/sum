<script setup lang="ts">
import { ThumbsUpIcon, ClockIcon } from 'lucide-vue-next';
import { AspectRatio } from '@/components/ui/aspect-ratio';

interface RestaurantCardProps {
  imageSrc?: string;
  name: string;
  rating: Number;
  openingHours: string;
  closingHours: string;
}

const props = defineProps<RestaurantCardProps>();
</script>

<template>
  <article class="bg-neutral-900 text-neutral-50 rounded text-xl space-y-3 p-4">
    <AspectRatio :ratio="16 / 9">
      <img
        src="https://images.unsplash.com/photo-1588345921523-c2dcdb7f1dcd?w=800&dpr=2&q=80"
        alt="Photo by Drew Beamer"
        class="rounded-md object-cover w-full h-full"
      />
    </AspectRatio>

    <header class="flex justify-between items-center">
      <p class="font-bold">{{ name }}</p>

      <div class="flex gap-3 items-center">
        <p>{{ rating }}%</p>
        <ThumbsUpIcon />
      </div>
    </header>

    <footer class="text-neutral-500">
      <p class="flex items-center gap-3 justify-end">
        {{ openingHours }} - {{ closingHours }}
        <ClockIcon />
      </p>
    </footer>
  </article>
</template>
