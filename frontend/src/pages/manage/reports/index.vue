<route lang="yaml">
meta:
  layout: manage
</route>

<script setup lang="ts">
import ReportsPage from '@/components/(manage)/reports/ReportsPage.vue';
import { useRestaurantUser } from '@/composables/useRestaurantUser';

const { user } = useRestaurantUser();
</script>

<template>
  <ReportsPage v-if="user" :restaurant-id="user.id" />
</template>
