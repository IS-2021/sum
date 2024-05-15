<script setup lang="ts">
import { useHead } from '@unhead/vue';
// import { getLatestBooking } from '.';
// import { getImageUrl } from '@/lib/assets';
// import { AspectRatio } from '@/components/ui/aspect-ratio';

useHead({
  title: 'Home',
});

// const latest = getLatestBooking();
// const restaurant = latest.restaurant;
// const latestMeal = latest.latestMeal;
// const imageSrc = restaurant?.imageUrl;
// const imgSrc = getImageUrl(imageSrc);
// const imageAltText = `${restaurant?.name} restaurant`;
</script>

<template>
  <div>
    <div class="w-full sm:max-w-screen-sm md:max-w-screen-md lg:max-w-screen-xl mx-auto px-4">
      <!-- <div v-if="latest">
        <h1 class="font-bold text-2xl mb-8">Last orders</h1>
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
            <p class="font-bold">{{ latestMeal.name }}</p>
          </header>
        </article>
      </div> -->
    </div>
  </div>
</template>
