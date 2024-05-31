<script setup lang="ts">
import type { IngredientDTO, MealDTO, Uuid } from '@/lib/api-model';
import AddIngredientPopup from '@/components/(manage)/meals/AddIngredientPopup.vue';
import { Button } from '@/components/ui/button';
import { useGetIngredients } from '@/lib/api/ingredients/ingredients';
import { computed, ref } from 'vue';
import { CircleCheckBigIcon, PlusIcon } from 'lucide-vue-next';
import { cn } from '@/lib/utils';

const props = defineProps<{
  meal: MealDTO;
  userId: Uuid;
}>();

const { data } = useGetIngredients();

const allIngredients = computed(() => data.value?.data ?? []);

const selectedIngredients = ref<IngredientDTO[]>(props.meal.ingredients ?? []);
const selectedIngredientsIds = computed(() => {
  const ids = selectedIngredients.value.map((i) => i.ingredientId);
  return new Set(ids);
});

function isSelected(ingredient: string) {
  return selectedIngredientsIds.value.has(ingredient);
}

function toggleIngredientSelect(ingredient: IngredientDTO) {
  if (selectedIngredientsIds.value.has(ingredient.ingredientId)) {
    selectedIngredients.value = selectedIngredients.value.filter(
      (i) => i.ingredientId !== ingredient.ingredientId,
    );
  } else {
    selectedIngredients.value = [...selectedIngredients.value, ingredient];
  }
}
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
      <ul class="space-y-2 mt-3">
        <li v-for="ingredient in allIngredients" :key="ingredient.ingredientId">
          <div
            :class="
              cn(
                'flex items-center gap-2 transition-colors',
                isSelected(ingredient.ingredientId) && 'text-primary',
              )
            "
          >
            <Button size="icon" variant="ghost" @click="toggleIngredientSelect(ingredient)">
              <CircleCheckBigIcon class="h-4 w-4" v-if="isSelected(ingredient.ingredientId)" />
              <PlusIcon class="h-4 w-4" v-else />
            </Button>

            <p>
              {{ ingredient.name }}
            </p>
          </div>
        </li>
      </ul>
    </section>
  </article>
</template>
