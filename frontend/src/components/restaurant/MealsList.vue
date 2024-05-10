<script setup lang="ts">
import { computed, ref } from 'vue';
import { useGetMeals } from '@/lib/api/meals/meals';
import { unref } from 'vue';
import { Button } from '@/components/ui/button';
import Filters from '@/components/restaurant/Filters.vue';
import type { Uuid } from '@/lib/api-model';

const props = defineProps<{
  categories: string[];
  restaurantId: Uuid;
}>();

const { data, isPending: areMealsLoading } = useGetMeals({
  restaurantId: props.restaurantId,
});
const meals = computed(() => unref(data)?.data);
const amount = ref(0);

const categories = ref(props.categories);
</script>

<template>
  <template v-if="areMealsLoading">
    <p>Loading...</p>
  </template>
  <div v-else-if="meals">
    <h1 class="text-2xl mt-16 font-bold">Available packages</h1>
    <div class="flex gap-12 mt-4">
      <div class="w-96 space-y-3 p-4 bg-neutral-900 rounded h-fit">
        <p>Excluding dishes that contain:</p>
        <Filters :restaurantId="restaurantId" />
      </div>
      <div
        class="mx-auto flex-grow space-y-3 mb-10"
        v-for="category in categories"
        v-bind:key="category"
      >
        <p v-if="meals.length === 0">No meals found</p>
        <div v-else v-for="meal in meals" v-bind:key="meal.mealId">
          <div class="bg-neutral-900 min-w-96 rounded p-4 space-y-3">
            <div class="flex">
              <h1 class="font-semibold text-xl">{{ meal.name }}</h1>
              <div class="flex-grow"></div>
              <p class="text-xs">Available amount: {{ amount }}</p>
            </div>
            <p class="text-neutral-300">{{ meal.description }}</p>
            <div class="flex flex-wrap">
              <p class="text-neutral-300 pr-1">Ingredients:</p>
              <!-- <IngredientsList :mealId="meal.id" /> -->
              <div
                class="text-neutral-300 pr-1"
                v-for="ingredient in meal.ingredients"
                :key="ingredient.id"
              >
                <p v-if="ingredient === meal.ingredients?.slice(-1)[0]">{{ ingredient.name }}</p>
                <p v-else>{{ ingredient.name }},</p>
              </div>
            </div>
            <Button class="w-1/2">Book</Button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
