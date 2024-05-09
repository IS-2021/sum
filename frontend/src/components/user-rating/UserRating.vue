<script setup lang="ts">
import { computeUserRating } from '@/components/user-rating/userRating';

const props = defineProps<{
  likes: number;
  dislikes: number;
}>();

const likesPercentage = computeUserRating(props.likes, props.dislikes);
</script>

<template>
  <span>{{ likesPercentage }}% ({{ likes + dislikes }})</span>
</template>
