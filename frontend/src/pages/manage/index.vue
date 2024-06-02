<route lang="yaml">
meta:
  layout: manage
</route>

<script setup lang="ts">
import RestaurantDashboard from '@/components/(manage)/dashboard/RestaurantDashboard.vue';
import { useRestaurantUser } from '@/composables/useRestaurantUser';

const { restaurant } = useRestaurantUser();
</script>

<template>
  <RestaurantDashboard v-if="restaurant?.id" :restaurant="restaurant" />
</template>
