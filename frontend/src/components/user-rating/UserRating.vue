<script setup lang="ts">
import { useUserRating } from '@/components/user-rating/useUserRating';

const props = defineProps<{
  likes: number;
  dislikes: number;
}>();

const likesPercentage = useUserRating(props.likes, props.dislikes);
</script>

<template>
  <span>{{ likesPercentage }} ({{ likes + dislikes }})</span>
</template>
