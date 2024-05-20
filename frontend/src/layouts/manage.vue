<script setup lang="ts">
import { useHead } from '@unhead/vue';
import Navbar from '@/components/(manage)/common/Navbar.vue';

useHead({
  title: 'FoodGood',
});
</script>

<template>
  <div class="flex flex-grow">
    <Navbar />
    <div class="flex-grow mx-16 mt-14">
      <RouterView />
    </div>
  </div>
</template>
