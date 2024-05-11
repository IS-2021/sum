<script setup lang="ts">
import type { RouterLinkProps as AutoRouterLinkProps } from 'vue-router/auto';
import type { RouterLinkProps } from 'vue-router';

type AppRouterLinkProps = Omit<RouterLinkProps, 'to' | 'activeClass' | 'exactActiveClass'> & {
  to: (typeof AutoRouterLinkProps)['to'];
};

const props = defineProps<AppRouterLinkProps>();
</script>

<template>
  <RouterLink :to="props.to" exact-active-class="text-primary">
    <slot />
  </RouterLink>
</template>
