<route lang="yaml">
meta:
  layout: manage
</route>

<script setup lang="ts">
import MealsPage from '@/components/(manage)/meals/MealsPage.vue';
import { useRestaurantUser } from '@/composables/useRestaurantUser';
import { useHead } from '@unhead/vue';

useHead({
  title: 'Meals',
});

const { user } = useRestaurantUser();
</script>

<template>
  <MealsPage v-if="user" :restaurantId="user.id" />
</template>
