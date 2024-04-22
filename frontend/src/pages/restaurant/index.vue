<route lang="yaml">
meta:
  layout: centered
</route>

<script setup lang="ts">
import StarItem from '@/components/StarItem.vue';
import LikeItems from '@/components/LikeItems.vue';
import { ref } from 'vue';

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

</script>

<template>
  <div class="container">
    <div class="flex flex-col">
      <div class="flex flex-row">
        <p class="pr-4 text-4xl">Nazwa restauracji</p>
        <StarItem :isFavourite="isFavourite" />
        <div class="flex-grow"></div>
        <LikeItems />
      </div>
      <p class="flex">Address</p>
    </div>
  </div>
</template>
