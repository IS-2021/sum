<script setup lang="ts">
import type { Uuid } from '@/lib/api-model';
import { ref } from 'vue';

import Button from '@/components/ui/button/Button.vue';
import { Trash2 } from 'lucide-vue-next';

import { getMeals } from '@/components/restaurant/restaurant';

const props = defineProps<{
  restaurantId: Uuid;
}>();

const meals = getMeals(props.restaurantId).meals;
const areMealsLoading = getMeals(props.restaurantId).areMealsLoading;
const amount = ref(0);

console.log(meals);
</script>

<template>
  <template v-if="areMealsLoading">
    <p>Loading...</p>
  </template>
  <div v-else-if="meals">
    <h1 class="text-2xl font-semibold tracking-tight mb-10">Meals</h1>
    <div class="flex flex-col lg:flex-row gap-8 mt-4">
      <div class="flex-grow space-y-3 mb-10">
        <p v-if="meals.length === 0">No meals found</p>
        <div
          v-else
          v-for="meal in meals"
          v-bind:key="meal.mealId"
          class="bg-neutral-200 rounded p-4 space-y-3"
        >
          <div class="flex justify-between">
            <h1 class="font-semibold text-xl">{{ meal.name }}</h1>
            <p class="text-xs">Available amount: {{ amount }}</p>
          </div>
          <p class="text-neutral-950">{{ meal.description }}</p>
          <div class="flex flex-wrap">
            <p class="text-neutral-800 pr-1">
              Ingredients: {{ meal.ingredients?.map((ingredient) => ingredient.name).join(', ') }}
            </p>
          </div>
          <div class="flex items-center gap-3 justify-between">
            <Button class="w-1/2">Modify</Button>
            <Trash2 class="cursor-pointer" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
