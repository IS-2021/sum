<script setup lang="ts">
import type { Uuid } from '@/lib/api-model';

import Button from '@/components/ui/button/Button.vue';
import { Plus } from 'lucide-vue-next';

import { getMeals } from '@/components/restaurant/restaurant';
import Meal from '@/components/(manage)/meals/Meal.vue';

const props = defineProps<{
  restaurantId: Uuid;
}>();

const meals = getMeals(props.restaurantId).meals;
const areMealsLoading = getMeals(props.restaurantId).areMealsLoading;
</script>

<template>
  <div class="flex justify-between items-center max-w-screen-md mb-10">
    <h1 class="text-2xl font-semibold tracking-tight">Meals</h1>

    <Button as-child>
      <RouterLink to="/manage/meals/add">
        <Plus width="16" height="16" class="mr-2" />
        Add meal
      </RouterLink>
    </Button>
  </div>

  <template v-if="areMealsLoading">
    <p>Loading...</p>
  </template>
  <div v-else-if="meals">
    <div class="space-y-3 mb-10 max-w-screen-md w-full">
      <p v-if="meals.length === 0">No meals found</p>
      <div v-else>
        <Meal
          v-for="meal in meals"
          v-bind:key="meal.mealId"
          :mealId="meal.mealId"
          :mealName="meal.name"
          :mealDescription="meal.description"
          :ingredients="meal.ingredients"
        />
      </div>
    </div>
  </div>
</template>
