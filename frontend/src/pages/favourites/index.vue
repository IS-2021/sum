<script setup lang="ts">
import { useUser } from '@/composables/useUser';
import FavouritesPage from '@/components/favourites/FavouritesPage.vue';
import { useHead } from '@unhead/vue';

useHead({
  title: 'Favourites',
});

const { user } = useUser();
</script>

<template>
  <div class="mx-auto w-full px-4 sm:max-w-screen-sm md:max-w-screen-md lg:max-w-screen-lg">
    <FavouritesPage v-if="user" :user="user" />
  </div>
</template>
