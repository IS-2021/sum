<script setup lang="ts">
import { useHead } from '@unhead/vue';
import Navbar from '@/components/(manage)/common/Navbar.vue';
import MobileNavbar from '@/components/(manage)/common/MobileNavbar.vue';
import Logo from '@/components/Logo.vue';

useHead({
  title: 'FoodGood',
});
</script>

<template>
  <div class="flex flex-col lg:flex-row flex-grow">
    <div
      class="lg:hidden gap-4 h-16 px-4 sm:px-8 lg:px-12 xl:px-16 py-4 flex items-center border-b border-neutral-300"
    >
      <MobileNavbar />
      <Logo class="h-8 w-8" />
    </div>

    <div class="hidden flex-shrink-0 lg:block w-[300px] border-r border-neutral-300">
      <Navbar />
    </div>

    <div class="flex-grow mx-4 sm:mx-8 lg:mx-12 xl:mx-16 mt-14">
      <RouterView />
    </div>
  </div>
</template>
