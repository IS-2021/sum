<script setup lang="ts">
import { useUserRating } from '@/components/user-rating/useUserRating';

const props = defineProps<{
  likes: number;
  dislikes: number;
}>();

const { ratingPercentage } = useUserRating(props.likes, props.dislikes);
</script>

<template>
  <span>{{ ratingPercentage }} ({{ likes + dislikes }})</span>
</template>
