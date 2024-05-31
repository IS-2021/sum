<route lang="yaml">
meta:
  layout: manage
</route>

<script setup lang="ts">
import RestaurantDashboard from '@/components/(manage)/dashboard/RestaurantDashboard.vue';
import { useRestaurantUser } from '@/composables/useRestaurantUser';

const { user } = useRestaurantUser();
</script>

<template>
  <RestaurantDashboard v-if="user" :restaurantId="user.id" />
</template>
