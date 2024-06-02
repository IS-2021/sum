<route lang="yaml">
meta:
  layout: restaurant
</route>

<script setup lang="ts">
import RestaurantComponent from '@/components/restaurant/RestaurantComponent.vue';
import { useUser } from '@/composables/useUser';

const { user } = useUser();
</script>

<template>
  <RestaurantComponent v-if="user" :userId="user.id" />
</template>
