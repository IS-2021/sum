<script setup lang="ts">
import { RouterView } from 'vue-router';
import { onMounted } from 'vue';
import { useAuthRedirect } from '@/composables/useAuthRedirect';
import { toast, Toaster } from 'vue-sonner';

onMounted(() => {
  // This is soo hacky, but I haven't found out why the class="dark" is attached to the html tag
  // after the JS is loaded. This is a workaround to remove the class after the JS is loaded.
  setTimeout(() => {
    document.documentElement.classList.remove('dark');
  }, 10);
});

useAuthRedirect({
  protectedRoutes: ['/favourites/', '/settings/'],
  onGuestRedirect: () => {
    toast.message('You need to sign-in before viewing this this page.');
  },
  guestRoutes: ['/sign-in/', '/sign-up/'],
});
</script>

<template>
  <Suspense>
    <RouterView />
  </Suspense>

  <Toaster position="top-center" rich-colors />
</template>
