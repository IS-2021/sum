<route lang="yaml">
meta:
  layout: centered
</route>

<script setup lang="ts">
import { useHead } from '@unhead/vue';
import SignUpForm from '@/components/sign-up/SignUpForm.vue';

useHead({
  title: 'Sign-Up',
});
</script>

<template>
  <div class="mx-auto border border-neutral-800 p-8 rounded">
    <h1 class="text-xl text-center font-bold mb-8">Sign-Up</h1>
    <SignUpForm />

    <p class="w-full text-sm text-center mt-4">
      Already have an account?
      <RouterLink to="/sign-in" class="underline">Sign in</RouterLink>
    </p>
  </div>
</template>
