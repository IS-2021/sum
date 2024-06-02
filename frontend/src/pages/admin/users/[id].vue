<route lang="yaml">
meta:
  layout: admin
</route>

<script setup lang="ts">
import { useHead } from '@unhead/vue';
import { useRoute } from 'vue-router/auto';

useHead({
  title: 'User',
});

const route = useRoute('/admin/restaurants/[id]');
const userId = route.params.id;
</script>

<template>
  <div>
    <h1 class="mb-10 text-2xl font-semibold tracking-tight">Username</h1>
  </div>
</template>
