<script setup lang="ts">
import { useUser } from '@/composables/useUser';

const { isSignedIn, isLoaded } = useUser();
</script>

<template>
  <div v-if="isSignedIn && isLoaded">
    <slot />
  </div>
</template>
