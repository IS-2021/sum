<route lang="yaml">
meta:
  layout: default
</route>

<script setup lang="ts">
import { useHead } from '@unhead/vue';
import { useUser } from '@/composables/useUser';

import BookingsComponent from '@/components/bookings/BookingsComponent.vue';

useHead({
  title: 'Bookings - FoodGood',
});

const { user } = useUser();
</script>

<template>
  <BookingsComponent v-if="user" :userId="user.id" :username="user.username" />
</template>
