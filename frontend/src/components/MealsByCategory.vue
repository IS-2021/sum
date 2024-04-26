<script setup lang="ts">
import { ref } from 'vue';

const props = defineProps<{
  categories: string[]
}>()

const categories = ref(props.categories)
</script>

<template>
  <div class="mt-10" v-for="category in categories" v-bind:key="category">
    <h1 class="text-2xl mb-4">{{ category }}</h1>
    <div class="w-full space-y-3">
      <div v-for="n in 1" v-bind:key="n" class="border h-40 min-w-80 rounded-lg"></div>
    </div>
  </div>
</template>
