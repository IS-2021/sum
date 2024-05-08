<script setup lang="ts">
import { computed, ref } from 'vue';
import { useGetMeals } from '@/lib/api/meals/meals';
import { unref } from 'vue';
import { Button } from '@/components/ui/button';
import TagsInput from '@/components/restaurant/TagsInput.vue';
import Filters from '@/components/restaurant/Filters.vue';
import type { Uuid } from '@/lib/api-model';
import IngredientsList from '@/components/ingredients/IngredientsList.vue';

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
        <TagsInput />
        <Filters :restaurantId="props.restaurantId" />
      </div>
      <div
        class="mx-auto flex-grow space-y-3 mb-10"
        v-for="category in categories"
        v-bind:key="category"
      >
        <p v-if="meals.length === 0">No meals found</p>
        <div v-else v-for="meal in meals" v-bind:key="meal.id">
          <div class="bg-neutral-900 min-w-96 rounded p-4 space-y-3">
            <div class="flex">
              <h1 class="font-semibold text-xl">{{ meal.name }}</h1>
              <div class="flex-grow"></div>
              <p class="text-xs">Available amount: {{ amount }}</p>
            </div>
            <p class="text-neutral-300">{{ meal.description }}</p>
            <p class="text-neutral-300">Ingredients</p>
            <IngredientsList :mealId="meal.id" />
            <Button class="w-1/2">Book</Button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
