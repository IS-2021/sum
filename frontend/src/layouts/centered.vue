<script setup lang="ts">
import Header from '@/components/Header.vue';
import Footer from '@/components/Footer.vue';
import { useHead } from '@unhead/vue';

useHead({
  title: 'FoodGood',
});
</script>

<template>
  <Header />
  <div class="grid flex-grow min-h-screen my-10">
    <RouterView />
  </div>
  <Footer />
</template>
