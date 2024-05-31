<script setup lang="ts">
import type { MealDTO, Uuid } from '@/lib/api-model';
import AddIngredientPopup from '@/components/(manage)/meals/AddIngredientPopup.vue';
import { Button } from '@/components/ui/button';
import { useGetIngredients } from '@/lib/api/ingredients/ingredients';
import { computed } from 'vue';

const props = defineProps<{
  meal: MealDTO;
  userId: Uuid;
}>();

const { data } = useGetIngredients();

const allIngredients = computed(() => data.value?.data ?? []);

const mealIngredientsIds = computed(() => {
  const ingredientsIds = (props.meal.ingredients ?? []).map(
    (ingredient) => ingredient.ingredientId,
  );
  return new Set(ingredientsIds);
});
</script>

<template>
  <header class="flex justify-between">
    <h1 class="text-2xl font-semibold tracking-tight mb-10">Add ingredients to your meal</h1>
    <AddIngredientPopup :mealId="meal.mealId" :userId="props.userId" />
  </header>

  <article class="bg-neutral-200 border p-4 rounded-md">
    <div class="flex gap-1 mb-2">
      <p><span class="font-bold">Name:</span> {{ meal.description }}</p>
    </div>
    <div class="flex gap-1">
      <p><span class="font-bold">Description:</span> {{ meal.description }}</p>
    </div>

    <section class="mt-10">
      <Button>Add new ingredient</Button>

      <h2 class="text-lg font-semibold mt-4">Ingredients</h2>
      <ul class="space-y-2 mt-3 list-disc ml-4">
        <li v-for="ingredient in allIngredients" :key="ingredient.ingredientId">
          {{ ingredient.name }}
          <p v-if="mealIngredientsIds.has(ingredient.ingredientId)">+</p>
        </li>
      </ul>
    </section>
  </article>
</template>
