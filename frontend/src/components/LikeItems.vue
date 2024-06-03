<script setup lang="ts">
import type { Uuid } from '@/lib/api-model';
import { deleteOpinions, useGetOpinions } from '@/lib/api/default/default';
import { postOpinions, putOpinionsId } from '@/lib/api/opinions/opinions';
import { ThumbsUp } from 'lucide-vue-next';
import { ThumbsDown } from 'lucide-vue-next';
import { computed, unref } from 'vue';

const props = defineProps<{
  userId: Uuid;
  restaurantId: Uuid;
}>();

const { data, refetch } = useGetOpinions({
  userId: props.userId,
  restaurantId: props.restaurantId,
});
const opinion = computed(() => unref(data));

const toggleLike = async () => {
  if (opinion.value?.status === 404) {
    const res = await postOpinions({
      isPositive: true,
      restaurantId: props.restaurantId,
      userId: props.userId,
    });

    if (res.status === 200) {
      refetch();
    }
  } else if (opinion.value?.data.isPositive === false) {
    const res = await putOpinionsId(opinion.value.data.opinionId, {
      isPositive: true,
      opinionId: opinion.value.data.opinionId,
      restaurantId: props.restaurantId,
      userId: props.userId,
    });

    if (res.status === 200) {
      refetch();
    }
  } else if (opinion.value?.data.isPositive === true) {
    const res = await deleteOpinions({ opinionId: opinion.value.data.opinionId });

    if (res.status === 200) {
      refetch();
    }
  }
};

const toggleDislike = async () => {
  if (opinion.value?.status === 404) {
    const res = await postOpinions({
      isPositive: false,
      restaurantId: props.restaurantId,
      userId: props.userId,
    });

    if (res.status === 200) {
      refetch();
    }
  } else if (opinion.value?.data.isPositive === true) {
    const res = await putOpinionsId(opinion.value.data.opinionId, {
      isPositive: false,
      opinionId: opinion.value.data.opinionId,
      restaurantId: props.restaurantId,
      userId: props.userId,
    });

    if (res.status === 200) {
      refetch();
    }
  } else if (opinion.value?.data.isPositive === false) {
    const res = await deleteOpinions({ opinionId: opinion.value.data.opinionId });

    if (res.status === 200) {
      refetch();
    }
  }
};
</script>

<template>
  <div class="flex flex-col items-end">
    <div class="w-40 h-10 border-gray-50 flex flex-row rounded-lg bg-neutral-200">
      <div class="flex items-center justify-center w-20">
        <ThumbsUp
          v-if="opinion && opinion?.status === 404"
          class="cursor-pointer"
          @click="toggleLike"
        />
        <ThumbsUp
          v-else-if="opinion && opinion.data.isPositive"
          class="cursor-pointer stroke-primary"
          @click="toggleLike"
        />
        <ThumbsUp
          v-else-if="opinion && !opinion.data.isPositive"
          class="cursor-pointer"
          @click="toggleLike"
        />
      </div>
      <div class="flex items-center justify-center w-20">
        <ThumbsDown v-if="opinion?.status === 404" class="cursor-pointer" @click="toggleDislike" />
        <ThumbsDown
          v-else-if="opinion && !opinion.data.isPositive"
          class="cursor-pointer stroke-red-500"
          @click="toggleDislike"
        />
        <ThumbsDown
          v-else-if="opinion && opinion.data.isPositive"
          class="cursor-pointer"
          @click="toggleDislike"
        />
      </div>
    </div>
  </div>
</template>
