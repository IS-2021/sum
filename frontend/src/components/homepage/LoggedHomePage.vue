<script setup lang="ts">
import { useHead } from '@unhead/vue';
import { useUser } from '@/composables/useUser';
import { nextTick } from 'vue';

import FavouritesDisplay from '@/components/homepage/FavouritesDisplay.vue';
import WelcomeComponent from '@/components/homepage/WelcomeComponent.vue';
import LatestBookings from '@/components/homepage/LatestBookings.vue';

useHead({
  title: 'Home',
});

nextTick();

const { user } = useUser();
</script>

<template>
  <div class="w-full sm:max-w-screen-sm md:max-w-screen-md lg:max-w-screen-xl mx-auto px-4">
    <WelcomeComponent v-if="user?.id" :user="user" />
    <LatestBookings v-if="user?.id" :userId="user.id" />
    <FavouritesDisplay v-if="user?.id" :userId="user.id" />
  </div>
</template>
