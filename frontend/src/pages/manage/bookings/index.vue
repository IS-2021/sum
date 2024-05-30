<route lang="yaml">
meta:
  layout: manage
</route>

<script setup lang="ts">
import BookingsPage from '@/components/(manage)/bookings/BookingsPage.vue';
import { useUser } from '@/composables/useUser';

const { user } = useUser();
</script>

<template>
  <BookingsPage v-if="user" :restaurantId="user?.id" />
</template>
