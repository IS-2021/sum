<route lang="yaml">
meta:
  layout: admin
</route>

<script setup lang="ts">
import { useRouter } from 'vue-router/auto';

const router = useRouter();
router.replace('/admin/restaurants');
</script>

<template>
  <div></div>
</template>
