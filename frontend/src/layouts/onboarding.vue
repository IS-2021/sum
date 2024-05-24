<script setup lang="ts">
import Logo from '@/components/Logo.vue';
</script>

<template>
  <div class="w-full grid grid-cols-1 md:grid-cols-2 max-h-svh">
    <div class="hidden md:block">
      <img
        src="@/assets/images/pablo-merchan-montes-Orz90t6o0e4-unsplash-crop.jpg"
        alt="FoodGood background image"
        class="w-full h-svh object-cover object-right-top aria-hidden"
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
