<script setup lang="ts">
import { RouterView } from 'vue-router';
import { onMounted } from 'vue';

onMounted(() => {
  // This is soo hacky, but I haven't found out why the class="dark" is attached to the html tag
  // after the JS is loaded. This is a workaround to remove the class after the JS is loaded.
  setTimeout(() => {
    document.documentElement.classList.remove('dark');
  }, 10);
});
</script>

<template>
  <Suspense>
    <RouterView />
  </Suspense>
</template>
