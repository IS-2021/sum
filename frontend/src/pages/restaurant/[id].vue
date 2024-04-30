<route lang="yaml">
meta:
  layout: default
</route>

<script setup lang="ts">
import StarItem from '@/components/StarItem.vue';
import LikeItems from '@/components/LikeItems.vue';
import MealsByCategory from '@/components/MealsByCategory.vue';
import Address from '@/components/RestaurantAddress.vue';

import { useRoute } from 'vue-router/auto';
import { ref } from 'vue';
import type { Ref } from 'vue';
import { unref } from 'vue';

import { useGetRestaurantsId } from '@/lib/api/restaurants/restaurants';


const route = useRoute('/restaurant/[id]')
const id = route.params.id;
const { data, isPending: areRestaurantsLoading } = useGetRestaurantsId(id);
const restaurant = unref(data)?.data;

const isFavourite = ref(false);
const categories = ref(['Kategoria 1'])
const isLiked: Ref<boolean | null> = ref(null);

function getRestaurant() {
  console.log(unref(data)?.data);
}

</script>

<template>
  <div class="container">
    <template v-if="areRestaurantsLoading">
      <p>Loading...</p>
    </template>
    <div class="flex flex-col">
      <div class="flex flex-row flex-wrap">
        <p class="font-semibold pr-4 text-4xl">{{ restaurant?.name }}</p>
        <StarItem :isFavourite="isFavourite" />
        <div class="flex-grow"></div>
        <LikeItems :isLiked="isLiked" @click="getRestaurant" />
      </div>
      <Address />
    </div>
    <MealsByCategory :categories="categories" />
  </div>
</template>
