<script setup lang="ts">
import { useUser } from '@/composables/useUser';
import FavouritesPage from '@/components/favourites/FavouritesPage.vue';
import { useHead } from '@unhead/vue';
import { useAuthRedirect } from '@/composables/useAuthRedirect';

const { user } = useUser();

useHead({
  title: 'Favourites',
});

useAuthRedirect({ redirectTo: '/sign-in', requireAuth: false });
</script>

<template>
  <div class="w-full sm:max-w-screen-sm md:max-w-screen-md lg:max-w-screen-xl mx-auto px-4">
    <FavouritesPage v-if="user" :user="user" />
  </div>
</template>
