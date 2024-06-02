<route lang="yaml">
meta:
  layout: admin
</route>

<script setup lang="ts">
import { useHead } from '@unhead/vue';
import RestaurantsPage from '@/components/(admin)/restaurants/RestaurantsPage.vue';

useHead({
  title: 'Admin - Restaurants',
});
</script>

<template>
  <RestaurantsPage />
</template>
