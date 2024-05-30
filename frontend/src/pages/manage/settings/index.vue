<route lang="yaml">
meta:
  layout: manage
</route>

<script setup lang="ts">
import SettingsPage from '@/components/(manage)/settings/SettingsPage.vue';
import { useRestaurantUser } from '@/composables/useRestaurantUser';
import { useHead } from '@unhead/vue';

useHead({
  title: 'Restaurant settings',
});

const { restaurant } = useRestaurantUser();
</script>

<template>
  <SettingsPage v-if="restaurant?.id" :restaurant="restaurant" />
</template>
