<script setup lang="ts">
import type { Uuid } from '@/lib/api-model';
import { useGetOpinions } from '@/lib/api/default/default';
import { postOpinions } from '@/lib/api/opinions/opinions';
import { ThumbsUp } from 'lucide-vue-next';
import { ThumbsDown } from 'lucide-vue-next';
import { computed, ref, unref, type Ref } from 'vue';

const props = defineProps<{
  userId: Uuid;
  restaurantId: Uuid;
}>();

const { data, refetch } = useGetOpinions({
  userId: props.userId,
  restaurantId: props.restaurantId,
});
const isRestaurantLiked = computed(() => unref(data)?.data);

const isLiked = ref(isRestaurantLiked);

const emit = defineEmits<{
  (e: 'isLikedChange', isLiked: Boolean | undefined): void;
}>();

const toggleLike = async () => {
  // !isLiked.value || isLiked.value === null ? (isLiked.value = true) : (isLiked.value = undefined);
  // emit('isLikedChange', isLiked.value);
  if (!isLiked.value) {
    const res = await postOpinions({
      isPositive: true,
      restaurantId: props.restaurantId,
      userId: props.userId,
    });

    if (res.status === 200) {
      refetch();
    }
  }
};

const toggleDislike = () => {
  isLiked.value || isLiked.value === null ? (isLiked.value = false) : (isLiked.value = undefined);
  emit('isLikedChange', isLiked.value);
};

console.log(isRestaurantLiked.value);
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
