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
const mealsWithIngredients = props.meals.filter((meal) => meal.ingredients.length !== 0);
let filteredMeals: Ref<MealDTO[]> = ref(mealsWithIngredients);

function updateFilters(list: IngredientDTO[]) {
  unwantedIngredients.value = list;
  filteredMeals.value = [];
  mealsWithIngredients.forEach((e) => {
    filteredMeals.value.push(e);
  });

  filteredMeals.value = mealsWithIngredients.filter((meal) => {
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
    <h1 class="mt-16 text-2xl font-bold">Available packages</h1>
    <div class="mt-4 flex flex-col gap-8 lg:flex-row">
      <div class="h-fit space-y-3 rounded bg-neutral-200 p-4 lg:sticky lg:top-20">
        <p>Excluding dishes that contain:</p>
        <Filters
          :restaurantId="restaurantId"
          :unwantedIngredients="unwantedIngredients"
          @filter-change="updateFilters"
        />
      </div>

      <div class="mb-10 flex-grow space-y-3">
        <p v-if="filteredMeals.length === 0">No meals found</p>
        <div
          v-else
          v-for="meal in filteredMeals"
          v-bind:key="meal.mealId"
          class="space-y-3 rounded bg-neutral-200 p-4"
        >
          <div>
            <h1 class="text-xl font-semibold">{{ meal.name }}</h1>
          </div>
          <p class="text-neutral-950">{{ meal.description }}</p>
          <div class="flex flex-wrap">
            <p class="pr-1 text-neutral-700">
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
