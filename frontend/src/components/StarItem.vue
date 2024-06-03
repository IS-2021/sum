<script setup lang="ts">
import { Star } from 'lucide-vue-next';
import { Button } from './ui/button';

const props = defineProps<{
  isFavourite: boolean;
}>();

const emit = defineEmits<{
  (e: 'favouriteChange', isFavourite: Boolean): void;
}>();

const toggleFavourite = () => {
  emit('favouriteChange', !props.isFavourite);
};
</script>

<template>
  <Button v-if="isFavourite" size="icon" variant="ghost" class="hover:!bg-opacity-40">
    <Star class="w-7 h-7 cursor-pointer fill-primary" @click="toggleFavourite" />
  </Button>
  <Button v-else size="icon" variant="ghost" class="hover:!bg-opacity-40">
    <Star class="w-7 h-7 cursor-pointer" @click="toggleFavourite" />
  </Button>
</template>
