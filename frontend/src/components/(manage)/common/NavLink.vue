<script setup lang="ts">
import type { RouterLinkProps as AutoRouterLinkProps } from 'vue-router/auto';
import type { RouterLinkProps } from 'vue-router';

type AppRouterLinkProps = Omit<RouterLinkProps, 'to' | 'activeClass' | 'exactActiveClass'> & {
  to: (typeof AutoRouterLinkProps)['to'];
};

const props = defineProps<AppRouterLinkProps>();
</script>

<template>
  <RouterLink
    :to="props.to"
    class="flex items-center gap-4 px-4 py-3 rounded"
    active-class="bg-secondary/50 text-accent"
  >
    <slot />
  </RouterLink>
</template>
