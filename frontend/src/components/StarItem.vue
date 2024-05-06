<script setup lang="ts">
import { Star } from 'lucide-vue-next';
import { ref } from 'vue';

const props = defineProps<{
  isFavourite: Boolean;
}>();

const emit = defineEmits<{
  (e: 'favouriteChange', isFavourite: Boolean): void;
}>();

const isFavourite = ref(props.isFavourite);

const toggleFavourite = () => {
  isFavourite.value = !isFavourite.value;
  emit('favouriteChange', isFavourite.value);
};
</script>

<template>
  <Star v-if="isFavourite" fill="white" class="w-7 h-7 cursor-pointer" @click="toggleFavourite" />
  <Star v-else class="w-7 h-7 cursor-pointer" @click="toggleFavourite" />
</template>
