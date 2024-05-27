<script setup lang="ts">
import Header from '@/components/navbar/Header.vue';
import { useHead } from '@unhead/vue';

useHead({
  title: 'FoodGood',
});
</script>

<template>
  <Header />
  <RouterView class="flex-grow" />
</template>
