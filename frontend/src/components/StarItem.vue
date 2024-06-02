<script setup lang="ts">
import { Star } from 'lucide-vue-next';
import { Button } from './ui/button';
import { ref } from 'vue';

const props = defineProps<{
  isFavourite: boolean;
}>();

const isFavourite = ref(props.isFavourite);

const emit = defineEmits<{
  (e: 'favouriteChange', isFavourite: Boolean): void;
}>();

const toggleFavourite = () => {
  isFavourite.value = !isFavourite.value;
  emit('favouriteChange', isFavourite.value);
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
