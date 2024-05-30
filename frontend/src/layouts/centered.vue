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
  <div class="grid items-center flex-grow min-h-screen my-10">
    <RouterView />
  </div>
  <Footer />
</template>
