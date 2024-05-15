<script setup lang="ts">
import { useHead } from '@unhead/vue';
// import { getLatestBooking } from '.';
// import { getImageUrl } from '@/lib/assets';
// import { AspectRatio } from '@/components/ui/aspect-ratio';
import { useUser } from '@/composables/useUser';
import FavouritesDisplay from '@/components/homepage/FavouritesDisplay.vue';

useHead({
  title: 'Home',
});

const { user } = useUser();

// const latest = getLatestBooking();
// const restaurant = latest.restaurant;
// const latestMeal = latest.latestMeal;
// const imageSrc = restaurant?.imageUrl;
// const imgSrc = getImageUrl(imageSrc);
// const imageAltText = `${restaurant?.name} restaurant`;
</script>

<template>
  <div class="w-full sm:max-w-screen-sm md:max-w-screen-md lg:max-w-screen-xl mx-auto px-4">
    <div
      class="flex h-56 rounded-md mb-8 bg-secondary items-center px-8 justify-center sm:justify-between flex-wrap"
    >
      <h1 class="text-5xl font-bold">FoodGood</h1>
      <div class="rounded-md w-full sm:w-1/2 flex flex-col justify-center items-center">
        <p v-if="user" class="text-2xl text-center py-3">Welcome {{ user.firstName }}!</p>
        <p class="text-2xl text-center py-3">Meals saved so far: 0</p>
      </div>
    </div>
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
    <div v-if="user">
      <FavouritesDisplay v-if="user" :user="user" />
    </div>
  </div>
</template>
