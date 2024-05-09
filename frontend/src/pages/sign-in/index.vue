<route lang="yaml">
meta:
  layout: auth
</route>

<script setup lang="ts">
import { useHead } from '@unhead/vue';
import SignInForm from '@/components/sign-in/SignInForm.vue';

useHead({
  title: 'Sign-In',
});
</script>

<template>
  <div>
    <h1 class="text-2xl font-bold mb-2">Sign in to FoodGood</h1>

    <p class="mb-8 text-neutral-700">Use your username and password to sign-in.</p>

    <div class="max-w-screen-sm w-full">
      <SignInForm />

      <p class="w-full text-sm text-center mt-4">
        Don't have an account?
        <RouterLink to="/sign-up" class="underline">Sign up</RouterLink>
      </p>
    </div>
  </div>
</template>
