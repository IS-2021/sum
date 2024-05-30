<script setup lang="ts">
import type { MealDTO, Uuid } from '@/lib/api-model';

import AddIngredientPopup from '@/components/(manage)/meals/AddIngredientPopup.vue';

const props = defineProps<{
  meal: MealDTO;
  userId: Uuid;
}>();
</script>

<template>
  <div class="flex justify-between">
    <h1 class="text-2xl font-semibold tracking-tight mb-10">Add ingredients to your meal</h1>
    <AddIngredientPopup :mealId="meal.mealId" :userId="props.userId" />
  </div>
  <article class="w-full bg-neutral-200 border p-4 rounded-md space-y-2">
    <div class="flex gap-1">
      <p class="font-bold">Name:</p>
      <p>{{ props.meal.name }}</p>
    </div>
    <div class="flex gap-1">
      <p class="font-bold">Description:</p>
      <p>{{ props.meal.description }}</p>
    </div>
  </article>
</template>
