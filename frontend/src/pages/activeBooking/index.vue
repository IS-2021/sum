<route lang="yaml">
meta:
  layout: default
</route>

<script setup lang="ts">
import { useHead } from '@unhead/vue';
import { useUser } from '@/composables/useUser';

import ActiveBookingComponent from '@/components/bookings/ActiveBookingComponent.vue';

useHead({
  title: 'Active Booking - FoodGood',
});

const { user } = useUser();
</script>

<template>
  <ActiveBookingComponent v-if="user" :userId="user.id" />
</template>
