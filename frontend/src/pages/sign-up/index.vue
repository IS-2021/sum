<route lang="yaml">
meta:
  layout: auth
</route>

<script setup lang="ts">
import { useHead } from '@unhead/vue';
import SignUpForm from '@/components/sign-up/SignUpForm.vue';

useHead({
  title: 'Sign-Up',
});
</script>

<template>
  <div>
    <h1 class="text-2xl font-bold mb-2">Sign up to FoodGood</h1>

    <p class="mb-8 text-neutral-700">Create yourself a new account to save food, for good.</p>

    <div class="max-w-screen-sm w-full">
      <SignUpForm />

      <p class="w-full text-sm text-center mt-4">
        Already have an account?
        <RouterLink to="/sign-in" class="underline">Sign in</RouterLink>
      </p>
    </div>
  </div>
</template>
