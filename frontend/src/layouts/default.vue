<script setup lang="ts">
import Header from '@/components/navbar/Header.vue';
import Footer from '@/components/Footer.vue';
import { useHead } from '@unhead/vue';

useHead({
  title: 'FoodGood',
});
</script>

<template>
  <Header />
  <RouterView class="min-h-screen my-8" />
  <Footer />
</template>
