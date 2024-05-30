<script setup lang="ts">
import { ref } from 'vue';
import { type Ref } from 'vue';

import Filters from '@/components/restaurant/Filters.vue';
import BookMealButton from '@/components/restaurant/BookMealButton.vue';

import Button from '../ui/button/Button.vue';

import type { IngredientDTO, MealDTO, Uuid } from '@/lib/api-model';
import { useUser } from '@/composables/useUser';

const props = defineProps<{
  categories: string[];
  restaurantId: Uuid;
  meals: MealDTO[];
  areMealsLoading: boolean;
}>();

const { user } = useUser();

const amount = ref(0);
const unwantedIngredients: Ref<IngredientDTO[]> = ref([]);
let filteredMeals: Ref<MealDTO[]> = ref(props.meals);

function updateFilters(list: IngredientDTO[]) {
  unwantedIngredients.value = list;
  filteredMeals.value = [];
  props.meals.forEach((e) => {
    filteredMeals.value.push(e);
  });

  filteredMeals.value = props.meals.filter((meal) => {
    const ingredients = meal.ingredients?.map((ingredient) => ingredient.name);
    return !unwantedIngredients.value.some((ingredient) => ingredients?.includes(ingredient.name));
  });
}
</script>

<template>
  <template v-if="areMealsLoading">
    <p>Loading...</p>
  </template>
  <div v-else-if="filteredMeals">
    <h1 class="text-2xl mt-16 font-bold">Available packages</h1>
    <div class="flex flex-col lg:flex-row gap-8 mt-4">
      <div class="space-y-3 p-4 bg-neutral-200 rounded h-fit">
        <p>Excluding dishes that contain:</p>
        <Filters
          :restaurantId="restaurantId"
          :unwantedIngredients="unwantedIngredients"
          @filter-change="updateFilters"
        />
      </div>
      <div class="flex-grow space-y-3 mb-10">
        <p v-if="filteredMeals.length === 0">No meals found</p>
        <div
          v-else
          v-for="meal in filteredMeals"
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
          <BookMealButton v-if="user" :mealId="meal.mealId" :userId="user.id" />
          <Button v-else-if="!user" class="bg-opacity-50" disabled>Book</Button>
        </div>
      </div>
    </div>
  </div>
</template>
