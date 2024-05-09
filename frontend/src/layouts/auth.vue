<script setup lang="ts">
import { useHead } from '@unhead/vue';
import Logo from '@/components/Logo.vue';

useHead({
  title: 'FoodGood',
});
</script>

<template>
  <div class="w-full grid grid-cols-1 sm:grid-cols-2 max-h-svh">
    <div class="hidden sm:block">
      <img
        src="@/assets/images/pablo-merchan-montes-Orz90t6o0e4-unsplash-crop.jpg"
        alt="FoodGood"
        class="w-full h-svh object-cover object-right-top"
      />
    </div>

    <div class="p-10 self-center">
      <RouterLink to="/">
        <Logo class="mb-8" />
      </RouterLink>
      <RouterView />
    </div>
  </div>
</template>
