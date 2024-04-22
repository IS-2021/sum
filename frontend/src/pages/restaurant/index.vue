<route lang="yaml">
meta:
  layout: centered
</route>

<script setup lang="ts">
import StarItem from '@/components/StarItem.vue';
import { ref } from 'vue';

const isFavourite = ref(false);
</script>

<template>
  <div>
    <div class="flex flex-row">
      <p class="px-4 text-4xl">Nazwa restauracji</p>
      <StarItem :isFavourite="isFavourite" />
    </div>
  </div>
</template>
