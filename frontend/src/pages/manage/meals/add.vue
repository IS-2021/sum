<route lang="yaml">
meta:
  layout: manage
</route>

<script setup lang="ts">
import MealsAddPage from '@/components/(manage)/meals/MealsAddPage.vue';
import { useUser } from '@/composables/useUser';
import { useHead } from '@unhead/vue';

useHead({
  title: 'Add meal',
});

const { user } = useUser();
</script>

<template>
  <MealsAddPage v-if="user" :userId="user.id" />
</template>
