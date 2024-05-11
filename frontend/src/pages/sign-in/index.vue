<route lang="yaml">
meta:
  layout: centered
</route>

<script setup lang="ts">
import { useHead } from '@unhead/vue';
import SignInForm from '@/components/sign-in/SignInForm.vue';

useHead({
  title: 'Sign-In',
});
</script>

<template>
  <div class="w-96 mx-auto border border-neutral-200 p-8 rounded">
    <h1 class="text-xl text-center font-bold mb-8">Sign-In</h1>

    <SignInForm />

    <p class="w-full text-sm text-center mt-4">
      Don't have an account?
      <RouterLink to="/sign-up" class="underline">Sign up</RouterLink>
    </p>
  </div>
</template>
