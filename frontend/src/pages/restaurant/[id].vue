<route lang="yaml">
meta:
  layout: default
</route>

<script setup lang="ts">
import StarItem from '@/components/StarItem.vue';
import LikeItems from '@/components/LikeItems.vue';
import MealsByCategory from '@/components/MealsByCategory.vue';
import { ref } from 'vue';
import type { Ref } from 'vue';

type UUID = string;

interface Restaurant {
  address: UUID,
  id: UUID,
  isActive: boolean,
  name: string,
  phoneNumber: string,
  userId: UUID,
  isFavourite: boolean
}

const props = defineProps<{
  restaurant: Restaurant
}>();

const isFavourite = ref(false);
const categories = ref(['Kategoria 1', 'Kategoria 2'])
const isLiked: Ref<boolean | null> = ref(null);

</script>

<template>
  <div class="container">
    <div class="flex flex-col">
      <div class="flex flex-row flex-wrap">
        <p class="pr-4 text-4xl">Nazwa restauracji</p>
        <StarItem :isFavourite="isFavourite" />
        <div class="flex-grow"></div>
        <LikeItems :isLiked="isLiked" />
      </div>
      <p class="flex">Address</p>
    </div>
    <MealsByCategory :categories="categories" />
  </div>
</template>
