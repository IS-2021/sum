<script setup lang="ts">
import { ThumbsUp } from 'lucide-vue-next';
import { ThumbsDown } from 'lucide-vue-next';
import { ref } from 'vue';

const props = defineProps<{
  isLiked: boolean | null
}>()

const isLiked = ref(props.isLiked);

const emit = defineEmits<{
  (e: 'isLikedChange', isLiked: Boolean | null): void
}>()

const toggleLike = () => {
  !isLiked.value || isLiked.value === null ? isLiked.value = true : isLiked.value = null;
  emit('isLikedChange', isLiked.value);
}

const toggleDislike = () => {
  isLiked.value || isLiked.value === null ? isLiked.value = false : isLiked.value = null;
  emit('isLikedChange', isLiked.value);
}
</script>

<template>
  <div class="flex flex-col items-end">
    <p>x%</p>
    <div class="border w-40 h-2 border-gray-50">
      <div class="bg-gray-50 w-20 h-full border-gray-50"></div>
    </div>
    <div class="w-40 h-10 border-gray-50 flex flex-row mt-3.5 rounded-lg bg-neutral-800">
      <div class="flex items-center justify-center w-20">
        <ThumbsUp v-if="isLiked === null" class="cursor-pointer" @click="toggleLike" />
        <ThumbsUp v-else-if="isLiked" fill="white" class="cursor-pointer" @click="toggleLike" />
        <ThumbsUp v-else-if="!isLiked" class="cursor-pointer" @click="toggleLike" />
      </div>
      <div class="flex items-center justify-center w-20">
        <ThumbsDown v-if="isLiked === null" class="cursor-pointer" @click="toggleDislike" />
        <ThumbsDown v-else-if="!isLiked" fill="white" class="cursor-pointer" @click="toggleDislike" />
        <ThumbsDown v-else-if="isLiked" class="cursor-pointer" @click="toggleDislike" />
      </div>
    </div>
  </div>
</template>
