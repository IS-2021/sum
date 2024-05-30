<route lang="yaml">
meta:
  layout: manage
</route>

<script setup lang="ts">
import MealsComponent from '@/components/(manage)/meals/MealsComponent.vue';
import { useRestaurantUser } from '@/composables/useRestaurantUser';

const { user } = useRestaurantUser();
</script>

<template>
  <MealsComponent v-if="user" :restaurantId="user.id" />
</template>
