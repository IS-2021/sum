<script setup lang="ts">
import Header from '@/components/Header.vue';
import { useHead } from '@unhead/vue';

useHead({
  title: 'FoodGood',
});
</script>

<template>
  <Header />
  <RouterView />
</template>
