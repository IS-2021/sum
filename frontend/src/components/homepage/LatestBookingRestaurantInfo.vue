<script setup lang="ts">
import type { Uuid } from '@/lib/api-model';
import { useGetRestaurantsId } from '@/lib/api/restaurants/restaurants';

import { computed, unref } from 'vue';

import RestaurantImageComponent from '@/components/homepage/RestaurantImageComponent.vue';

const props = defineProps<{
  restaurantId: Uuid;
  mealName: string;
}>();

const { data } = useGetRestaurantsId(props.restaurantId);
const restaurant = computed(() => unref(data)?.data);
</script>

<template>
  <article v-if="restaurant">
    <RestaurantImageComponent :imageUrl="restaurant.imageUrl" :restaurantName="restaurant.name" />

    <header class="flex flex-col justify-between items-start">
      <div class="flex">
        <p class="font-bold mr-1">Restaurant:</p>
        <p>{{ restaurant.name }}</p>
      </div>
      <div class="flex">
        <p class="font-bold mr-1">Meal:</p>
        <p>{{ props.mealName }}</p>
      </div>
    </header>
  </article>
</template>
