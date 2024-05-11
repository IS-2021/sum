<script setup lang="ts">
import { useUser } from '@/composables/useUser';
import { watchEffect } from 'vue';
import { useRouter } from 'vue-router/auto';

type RequireAuthProps = {
  redirect?: boolean;
};

const props = defineProps<RequireAuthProps>();

const router = useRouter();
const { isSignedIn, isLoaded } = useUser();

watchEffect(() => {
  if (isLoaded.value && !isSignedIn.value) {
    if (props.redirect) {
      router.push('sign-in');
    }
  }
});
</script>

<template>
  <div v-if="isSignedIn">
    <slot />
  </div>
</template>
