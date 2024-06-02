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
  <h1 class="text-2xl font-semibold tracking-tight mb-10">Reports</h1>
  <ReportsPage v-if="user" :restaurant-id="user.id" />
</template>
