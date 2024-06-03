<script setup lang="ts">
import type { UserMeDTO } from '@/lib/api-model';
import { StarIcon } from 'lucide-vue-next';
import { getImageUrl } from '@/lib/assets';
import { cn } from '@/lib/utils';
import { VueDraggable } from 'vue-draggable-plus';
import { useFavourites } from '@/components/favourites/useFavourites';
import FavouriteItem from '@/components/favourites/FavouriteItem.vue';

const { user } = defineProps<{ user: UserMeDTO }>();

const { favourites, hasAnyFavourites, handleDeleteFavourite, isDragDisabled } = useFavourites({
  userId: user.id,
});
</script>

<template>
  <h1 class="mb-8 text-2xl font-bold">Your Favourite Restaurants</h1>

  <div v-if="!hasAnyFavourites">
    <p>
      You don't have any favourite restaurants yet. Click on the
      <StarIcon class="inline-block" /> next to the restaurant to make it appear here.
    </p>
  </div>

  <div v-if="favourites">
    <VueDraggable
      :class="cn('space-y-4', isDragDisabled && 'animate-pulse opacity-80 transition-all')"
      v-model="favourites"
      handle=".handle"
      :animation="250"
      :disabled="isDragDisabled"
    >
      <li
        v-for="favourite in favourites"
        :key="favourite.id"
        :style="`--image-url: url('${getImageUrl(favourite.imageUrl)}')`"
        :class="cn('group list-none rounded bg-cover', `bg-[image:var(--image-url)]`)"
      >
        <FavouriteItem :favourite="favourite" :handle-delete-favourite="handleDeleteFavourite" />
      </li>
    </VueDraggable>
  </div>
</template>
