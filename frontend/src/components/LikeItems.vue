<script setup lang="ts">
import type { Uuid } from '@/lib/api-model';
import { ThumbsUp } from 'lucide-vue-next';
import { ThumbsDown } from 'lucide-vue-next';
import { ref, type Ref } from 'vue';

const props = defineProps<{
  userId: Uuid;
  restaurantId: Uuid;
}>();

const isLiked: Ref<boolean | null> = ref(null);

const emit = defineEmits<{
  (e: 'isLikedChange', isLiked: Boolean | null): void;
}>();

const toggleLike = () => {
  !isLiked.value || isLiked.value === null ? (isLiked.value = true) : (isLiked.value = null);
  emit('isLikedChange', isLiked.value);
};

const toggleDislike = () => {
  isLiked.value || isLiked.value === null ? (isLiked.value = false) : (isLiked.value = null);
  emit('isLikedChange', isLiked.value);
};
</script>

<template>
  <div class="flex flex-col items-end">
    <div class="w-40 h-10 border-gray-50 flex flex-row mt-3.5 rounded-lg bg-neutral-200">
      <div class="flex items-center justify-center w-20">
        <ThumbsUp v-if="isLiked === null" class="cursor-pointer" @click="toggleLike" />
        <ThumbsUp v-else-if="isLiked" class="cursor-pointer stroke-primary" @click="toggleLike" />
        <ThumbsUp v-else-if="!isLiked" class="cursor-pointer" @click="toggleLike" />
      </div>
      <div class="flex items-center justify-center w-20">
        <ThumbsDown v-if="isLiked === null" class="cursor-pointer" @click="toggleDislike" />
        <ThumbsDown
          v-else-if="!isLiked"
          class="cursor-pointer stroke-red-500"
          @click="toggleDislike"
        />
        <ThumbsDown v-else-if="isLiked" class="cursor-pointer" @click="toggleDislike" />
      </div>
    </div>
  </div>
</template>
