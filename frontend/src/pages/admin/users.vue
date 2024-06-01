<route lang="yaml">
meta:
  layout: admin
</route>

<script setup lang="ts">
import { useHead } from '@unhead/vue';

useHead({
  title: 'Admin - Users',
});
</script>

<template>
  <div>
    <h1 class="text-2xl font-semibold tracking-tight mb-10">Users</h1>
  </div>
</template>
