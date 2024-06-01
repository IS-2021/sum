<route lang="yaml">
meta:
  layout: admin
</route>

<script setup lang="ts">
import { useHead } from '@unhead/vue';

useHead({
  title: 'Admin - Restaurants',
});
</script>

<template>
  <div>
    <h1 class="text-2xl font-semibold tracking-tight mb-10">Restaurants</h1>
  </div>
</template>
